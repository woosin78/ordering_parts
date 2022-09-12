package org.jwebppy.portal.iv.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.service.IvPartsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH + "/parts")
public class IvPartsCommonController extends IvGeneralController
{
    @Value("${file.upload.rootPath}")
    private String rootPath;

    @Value("${file.upload.temp}")
    private String tempPath;

    private String uploadPath;

	@Autowired
	private IvPartsCommonService ivPartsCommonService;

    @PostConstruct
    public void init()
    {
    	uploadPath = rootPath + File.separator + tempPath;

    	try
    	{
			FileUtils.forceMkdir(new File(uploadPath));
		}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    }

	@RequestMapping("/search/data")
	@ResponseBody
	public Object searchData(@RequestParam Map<String, Object> paramMap)
	{
		String pPartNo = CmStringUtils.upperCase(CmStringUtils.trimToEmpty(paramMap.get("pPartNo")));

		if ("".equals(pPartNo))
		{
			return EMPTY_RETURN_VALUE;
		}

		ErpDataMap erpDataMap = getErpUserInfoByUsername();

		Map<String, String> rfcParamMap = new HashMap<>();
		rfcParamMap.put("partNo", pPartNo);
		rfcParamMap.put("partDesc", pPartNo);
		rfcParamMap.put("salesOrg", erpDataMap.getSalesOrg());
		rfcParamMap.put("distChannel", erpDataMap.getDistChannel());
		rfcParamMap.put("division", erpDataMap.getDivision());
		rfcParamMap.put("lang", erpDataMap.getLangForSap());

		return ivPartsCommonService.getPartsInfo(rfcParamMap);
	}

	@RequestMapping("/search/upload/data")
	@ResponseBody
	public Object upload(@RequestParam(value = "file", required = false) MultipartFile multipartFile)
	{
		if (CmStringUtils.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", multipartFile.getContentType()))
		{
			try
			{
				String fileName = getUsername() + "_" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

				multipartFile.transferTo(new File(uploadPath + File.separator + fileName));

				FileInputStream fileInputStream = null;

				try
				{
					File file = new File(uploadPath + File.separator + fileName);

					if (file.exists())
					{
						fileInputStream = new FileInputStream(file);

						return getExcelValues(fileInputStream);
					}
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				finally
				{
					fileInputStream.close();
					fileInputStream = null;
				}
			}
			catch (IllegalStateException | IOException e)
			{
				e.printStackTrace();
			}
		}

		return EMPTY_RETURN_VALUE;
	}

	@GetMapping("/search/dealer/data")
	@ResponseBody
	public Object targetData(@RequestParam("pName") String pName, @RequestParam("pCode") String pCode)
	{
		ErpDataMap erpDataMap = getErpUserInfoByUsername();

		Map<String, String> rfcParamMap = new HashMap<>();
		rfcParamMap.put("name", pName);
		rfcParamMap.put("dealerCode", pCode);
		rfcParamMap.put("salesOrg", erpDataMap.getSalesOrg());
		rfcParamMap.put("distChannel", erpDataMap.getDistChannel());
		rfcParamMap.put("division", erpDataMap.getDivision());

		return ivPartsCommonService.getDealers(rfcParamMap);
	}

	private List<String> getExcelValues(InputStream inputStream)
	{
		XSSFWorkbook xssfWorkbook = null;
		List<String> values = new LinkedList<>();

		try
		{
			xssfWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

			for (int i=0, size=xssfSheet.getPhysicalNumberOfRows(); i<size; i++)
			{
				XSSFCell xssfCell = xssfSheet.getRow(i).getCell(0);

				values.add(CmStringUtils.trimToEmpty(xssfCell.getStringCellValue()));
			}

			xssfWorkbook.close();
			xssfWorkbook = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (xssfWorkbook != null)
			{
				try
				{
					xssfWorkbook.close();
					xssfWorkbook = null;
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return values;
	}
}
