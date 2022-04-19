package org.jwebppy.portal.iv.hq.parts.domestic.claim.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.claim.service.ClaimCreateService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.sapportals.connector.ConnectorException;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/claim/create")
public class ClaimCreateController extends PartsDomesticGeneralController
{
	@Autowired
	private ClaimCreateService claimCreateService;

	@Autowired
	private Environment environment;

	// 반품생성 메인페이지
	@RequestMapping("/write")
	public String form(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	// 반품이유1 조회
	@RequestMapping("/reason/{level}")
	@ResponseBody
	public Object reasonData(@PathVariable String level, @RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap
			.add(new Object[][] {
				{"level", level},
				{"reason1", paramMap.get("reason1")},
			});

		RfcResponse rfcResponse = claimCreateService.getClaimReasons(rfcParamMap);

		if (CmStringUtils.equals(level, "1"))
		{
			return rfcResponse.getTable("T_COMPLAIN");
		}
		else
		{
			return rfcResponse.getTable("T_RETURN");
		}
	}

	// 반품 생성 첨부파일을 SAP에 전송하기 전, 자체 스토리지에 저장해 둔다.
	@PostMapping("/attachment/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile) throws IOException
    {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("ORIGIN_FILE_NAME", multipartFile.getOriginalFilename());
		resultMap.put("TEMP_FILE_NAME", claimCreateService.saveTempFile(multipartFile));
		resultMap.put("CONTENT_TYPE", multipartFile.getContentType());
		resultMap.put("SIZE", multipartFile.getSize());

        return resultMap;
    }

	// 반품 생성
	@RequestMapping("/create")
	@ResponseBody
	public Object create(HttpServletRequest request) throws ConnectorException, FileNotFoundException, IOException
	{
		RfcResponse rfcResponse = claimCreateService.create(request, getErpUserInfo(), getComplaintFileUploadPath());

		String errorMsg = "";	// 에러 메시지
		String complaintNo = "";	// SAP 반품번호
		String sapReturnFlag = rfcResponse.getString("RETURN2");	// S일 경우 성공, E 실패

		if (sapReturnFlag.equals("S"))
		{
			complaintNo = rfcResponse.getString("LV_VENLR").trim();	// SAP 반품번호
		} else
		{
			complaintNo = "NO_LV_VENLR";
			errorMsg = rfcResponse.getString("E_MEG");
		}

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("complaintNo", complaintNo);
		resultMap.put("errorMsg", errorMsg);

		return resultMap;
	}

	// 파일 경로 반환
	private String getComplaintFileUploadPath()
	{
		File path = new File(environment.getProperty("file.upload.rootPath") + File.separator + "complaint" + File.separator);

		if (!path.exists())
		{
			path.mkdirs();
		}
		return path.getAbsolutePath() + File.separator;
	}
}
