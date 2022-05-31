package org.jwebppy.portal.iv.uk.parts.domestic.complaint.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.common.web.UkPartsDomesticGeneralController;
import org.jwebppy.portal.iv.uk.parts.domestic.complaint.service.UkComplaintCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH + "/complaint/create")
@PreAuthorize("!hasRole('ROLE_DP_EUDO_PARTS_READ_ONLY_DEALER')")
public class UkComplaintCreationController extends UkPartsDomesticGeneralController
{
	@Autowired
	private UkComplaintCreationService ukComplaintCreationService;

//	@Autowired
//	private Environment environment;

    @Value("${file.upload.rootPath}")
    private String rootPath;

    @Value("${file.upload.claim}")
    private String claimPath;

    private String uploadPath;

    @PostConstruct
    public void init()
    {
    	uploadPath = rootPath + File.separator + claimPath;

    	try
    	{
			FileUtils.forceMkdir(new File(uploadPath));
		}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    }

	// 반품생성 메인페이지
	@RequestMapping("/complaint_form")
	public String complaintForm(Model model, WebRequest webRequest)
	{
		model.addAttribute("currUserName", getErpUserInfo().getUsername());
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	//반품생성 인보이스 팝업화면 호출
	@RequestMapping("/invoice_search_form")
	public String invoiceSearchForm(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	// Invoice 조회
	@RequestMapping("/invoice_search")
	@ResponseBody
	public Object invoiceSearch(@RequestParam Map<String, Object> parameterMap)
	{
		RfcResponse rfcResponse = null;
		Map<String, Object> resultMap = new HashMap<>();

		ErpDataMap paramMap = getErpUserInfo();

		if ( CmStringUtils.isEmpty( parameterMap.get("pInvoiceNo") ) )
		{
			resultMap.put("resultOrderList", new DataList());
		} else
		{
			paramMap.put("VBELN", parameterMap.get("pInvoiceNo"));
			rfcResponse = ukComplaintCreationService.getInvoiceInfoBySap(paramMap);
			DataList resultOrderList = rfcResponse.getTable("LT_SEARCH2");
			resultMap.put("resultOrderList", resultOrderList);
		}

		return resultMap;
	}

	// 반품이유1 조회
	@RequestMapping("/reason_search1")
	@ResponseBody
	public Object reasonSearch1(@RequestParam Map<String, Object> parameterMap)
	{
		RfcResponse rfcResponse = null;
		ErpDataMap paramMap = getErpUserInfo();
		paramMap.put("COMP_RES_CD", "1");
		rfcResponse = ukComplaintCreationService.getComplatintReasonInfoBySap(paramMap);

		DataList resultDataList = rfcResponse.getTable("T_COMPLAIN");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("returnReasonList1", resultDataList);

		return resultMap;
	}


	// 반품이유2 조회
	@RequestMapping("/reason_search2")
	@ResponseBody
	public Object reasonSearch2(@RequestParam Map<String, Object> parameterMap)
	{
		RfcResponse rfcResponse = null;
		ErpDataMap paramMap = getErpUserInfo();
		paramMap.put("I_ZZCMPCD", parameterMap.get("complaintReason1"));
		paramMap.put("COMP_RES_CD", "2");
		rfcResponse = ukComplaintCreationService.getComplatintReasonInfoBySap(paramMap);

		DataList resultDataList = rfcResponse.getTable("T_RETURN");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("returnReasonList2", resultDataList);

		return resultMap;
	}

	// 반품 생성 첨부파일을 SAP에 전송하기 전, 자체 스토리지에 저장해 둔다.
	@PostMapping("/complaint_file_upload")
	@ResponseBody
	public Object complaintFileUpload(@RequestParam(value = "file", required = false) MultipartFile mFile) throws IOException
    {
		String serverFileNm = ukComplaintCreationService.makeComplaintCreateFile(mFile, getComplaintFileUploadPath(), getErpUserInfo().getUsername());

		// 2. 파일 정보를 화면 파일 리스트에 표시하기 위한 처리
        StringBuilder sb = new StringBuilder();
        sb.append(mFile.getOriginalFilename()).append(":");
        sb.append(serverFileNm).append(":");
        sb.append(mFile.getContentType()).append(":");
        sb.append(mFile.getSize());

        return sb.toString();
    }

	// 반품 생성 첨부파일을 자체 스토리지에서 삭제한다.
	@RequestMapping("/complaint_file_delete")
	@ResponseBody
	public Object complaintFileDelete(@RequestParam("delfileName") String[] delfileNames) throws IOException
    {
		String serverFileNm = ukComplaintCreationService.deleteComplaintCreateFile(getComplaintFileUploadPath(), delfileNames);

        return serverFileNm;
    }

	// 반품 생성
	@RequestMapping("/complaint_create")
	@ResponseBody
	public Object complaintCreate(HttpServletRequest request, @RequestParam Map<String, Object> parameterMap) throws ConnectorException, FileNotFoundException, IOException
	{
		RfcResponse rfcResponse = ukComplaintCreationService.sendComplaintInfoToSAP(request, getErpUserInfo(), getComplaintFileUploadPath());

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
		/*
		File path = new File(environment.getProperty("file.upload.rootPath") + File.separator + "complaint" + File.separator);

		if (!path.exists())
		{
			path.mkdirs();
		}
		return path.getAbsolutePath() + File.separator;
		*/
		return uploadPath + File.separator;
	}
}