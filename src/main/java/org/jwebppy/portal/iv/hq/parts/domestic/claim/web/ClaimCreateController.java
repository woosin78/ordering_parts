package org.jwebppy.portal.iv.hq.parts.domestic.claim.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.claim.service.ClaimCreateService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Value("${file.upload.divk.domestic.parts.claim}")
    private String UPLOAD_PATH;

    @Value("${file.upload.invalidExtension}")
    private String INVALID_UPLOAD_FILE_EXTENSION;

	// 반품생성 메인페이지
	@RequestMapping("/write")
	public String form(Model model, WebRequest webRequest)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.add("depth", "1");

		RfcResponse rfcResponse = claimCreateService.getClaimReasons(rfcParamMap);

		model.addAttribute("reason1", rfcResponse.getTable("T_COMPLAIN"));
		model.addAttribute("reason2", rfcResponse.getTable("T_RETURN"));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	// 반품 생성 첨부파일을 SAP에 전송하기 전, 자체 스토리지에 저장해 둔다.
	@PostMapping("/attachment/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile) throws IOException
    {
		String originalFileName = multipartFile.getOriginalFilename();

		System.err.println("name: " + originalFileName);
		System.err.println(CmStringUtils.split(INVALID_UPLOAD_FILE_EXTENSION, PortalCommonVo.DELIMITER));

		for (String a: CmStringUtils.split(INVALID_UPLOAD_FILE_EXTENSION, PortalCommonVo.DELIMITER))
		{
			System.err.println("ext:" + a);
		}

		if (!FilenameUtils.isExtension(originalFileName, CmStringUtils.split(INVALID_UPLOAD_FILE_EXTENSION, PortalCommonVo.DELIMITER)))
		{
			try
			{
				File path = new File(UPLOAD_PATH);

				if (!path.exists())
				{
					path.mkdir();
				}

				String fileName = getUsername() + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

				multipartFile.transferTo(new File(UPLOAD_PATH + File.separator + fileName));

				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("ORIGIN_FILE_NAME", originalFileName);
				resultMap.put("SAVED_FILE_NAME", fileName);
				resultMap.put("CONTENT_TYPE", multipartFile.getContentType());
				resultMap.put("SIZE", multipartFile.getSize());

				return resultMap;
			}
			catch (IllegalStateException | IOException e)
			{
				e.printStackTrace();
			}
		}

        return EMPTY_RETURN_VALUE;
    }

	@RequestMapping("/save")
	@ResponseBody
	public Object save(HttpServletRequest request) throws ConnectorException, FileNotFoundException, IOException
	{
		RfcResponse rfcResponse = claimCreateService.create(request, getErpUserInfo());

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("CLAIM_NO", rfcResponse.getString("LV_VENLR"));
		resultMap.put("ERROR_MSG", rfcResponse.getString("E_MEG"));

		return resultMap;
	}
}
