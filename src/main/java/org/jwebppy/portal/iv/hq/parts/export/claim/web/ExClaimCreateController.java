package org.jwebppy.portal.iv.hq.parts.export.claim.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.claim.service.ExClaimCreateService;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.web.PartsExportGeneralController;
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
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/claim/create")
public class ExClaimCreateController extends PartsExportGeneralController
{
	@Autowired
	private ExClaimCreateService claimCreateService;

    @Value("${file.upload.rootPath}")
    private String rootPath;

    @Value("${file.upload.claim}")
    private String claimPath;

    @Value("${file.upload.invalidExtension}")
    private String invalidUploadFileExtension;

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

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest)
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

		if (!FilenameUtils.isExtension(originalFileName, CmStringUtils.split(invalidUploadFileExtension, PortalCommonVo.DELIMITER)))
		{
			try
			{
				String fileName = getUsername() + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

				multipartFile.transferTo(new File(uploadPath + File.separator + fileName));

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

	@RequestMapping("/summary")
	@ResponseBody
	public Object summary(Model model, WebRequest webRequest)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		DataMap dataMap = claimCreateService.getSummary(rfcParamMap).getStructure("O_ZSSS0075");

		dataMap.put("A_PRICE", dataMap.getDouble("O_AMT") + dataMap.getDouble("R_AMT"));
		dataMap.put("A_ROW", dataMap.getInt("O_ROW") + dataMap.getInt("R_ROW"));

		FormatBuilder.with(dataMap)
			.decimalFormat(new String[] {"B_AMT", "L_AMT", "A_AMT", "A_PRICE", "AO_AMT"})
			.qtyFormat(new String[] {"A_ROW", "AO_ROW"});

		return dataMap;
	}
}
