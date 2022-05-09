package org.jwebppy.portal.iv.hq.parts.export.claim.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sapportals.connector.ConnectorException;

@Service
public class ExClaimCreateService extends PartsExportGeneralService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

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

    public RfcResponse getSummary(PartsErpDataMap paramMap)
    {
    	RfcRequest rfcRequest = new RfcRequest("Z_SS_CLAIM_SUMMARY_INFO");

    	rfcRequest.field("I_BNAME", paramMap.getUsername());

    	return simpleRfcTemplate.response(rfcRequest);
    }

	public RfcResponse getClaimReasons(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SD_GET_RETURN_REASON");

		rfcRequest.
			field()
				.add(new String[][] {
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_VKORG", paramMap.getSalesOrg()}
				});

		if (paramMap.isEquals("depth", "2"))
		{
			rfcRequest.addField("LS_ZZCMPCD", paramMap.getString("reason1"));// Reason1 사유
		}

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse create(HttpServletRequest request, PartsErpDataMap paramMap) throws ConnectorException, FileNotFoundException, IOException
	{
		String[] orderNos = request.getParameterValues("orderNo");
		String[] lineNos = request.getParameterValues("lineNo");
		String[] partNos = request.getParameterValues("partNo");
		String[] reason1s = request.getParameterValues("reason1");
		String[] reason2s = request.getParameterValues("reason2");
		String[] descriptions = request.getParameterValues("description");
		String[] reqQties = request.getParameterValues("reqQty");
		String[] orderDates = request.getParameterValues("orderDate");

		RfcRequest rfcRequest = new RfcRequest("Z_EP_COMPLAIN_SAVE");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_USERID", paramMap.getUsername()},
					{"I_LANGU", paramMap.getLang()},
					{"BSTNK", request.getParameter("poNo")},
					{"I_BGTYP", "P"},
					{"I_KVGR5", paramMap.getCustomerGrp5()}
				});

		if (ArrayUtils.isNotEmpty(partNos))
		{
			List<Map<String, Object>> itemList = new ArrayList<>();
			List<Map<String, Object>> attachmentList = new ArrayList<>();

			for (int i=0, length=partNos.length; i<length; i++)
			{
				Map<String, Object> dataMap = new HashMap<String, Object>();

				String lineNo = CmStringUtils.leftPad((i+1)*10, 6, "0");//000010

				dataMap.put("CCHECK", (i+1));
				dataMap.put("ITEM", lineNo);//000010
				dataMap.put("MATERIAL", partNos[i]);
				dataMap.put("COMPLAINT", reason1s[i]);
				dataMap.put("REASON", reason2s[i]);
				dataMap.put("COMPLAINT_DESC", descriptions[i]);
				dataMap.put("QTY", reqQties[i]);
				dataMap.put("REQ_QTY", reqQties[i]);
				dataMap.put("ZZDEALDT", CmDateFormatUtils.stripDateFormat(orderDates[i]));
				dataMap.put("REF_ORD", orderNos[i]);
				dataMap.put("REF_ITEM", lineNos[i]);

				itemList.add(dataMap);

				String[] attachments = request.getParameterValues("attachment" + "_" + orderNos[i] + "_" + lineNos[i]);

				if (ArrayUtils.isNotEmpty(attachments))
				{
					final int BUFFER = 1024;
					byte[] buffer = new byte[BUFFER];

					for (int j=0, length2=attachments.length; j<length2; j++)
					{
						//0: original file name, 1: saved file name
						String[] fileNames = CmStringUtils.split(attachments[j], PortalCommonVo.DELIMITER);

						if (ArrayUtils.isNotEmpty(fileNames) && fileNames.length >= 2)
						{
							FileInputStream fileInputStream = null;
							BufferedInputStream bufferedInputStream = null;

	            			try
	            			{
	            				fileInputStream = new FileInputStream(uploadPath + File.separator + fileNames[1]);

	            				//송신 파일 설정 1024byte 씩 끊어서 append 한다.
	            				bufferedInputStream = new BufferedInputStream(fileInputStream, BUFFER);

	                            int seq = 1;

	                            while (bufferedInputStream.read(buffer) != -1)
	                            {
	                                HashMap<String, Object> fileDataMap = new HashMap<>();

	                                fileDataMap.put("INT_NO", (j+1));
	                                fileDataMap.put("DOCU_ITEM", lineNo);
	                                fileDataMap.put("FILE_NAME", fileNames[0]);
	                                fileDataMap.put("DESCRIPTION", "Complaint_Req");
	                                fileDataMap.put("FILE_NO", seq++);
	                                fileDataMap.put("FILE_DATA", buffer);

	                                attachmentList.add(fileDataMap);

	                                buffer = new byte[BUFFER];
	                            }

	                            bufferedInputStream.close(); bufferedInputStream = null;
	                            fileInputStream.close(); fileInputStream = null;
	            			}
	            			finally
	            			{
	            				if (bufferedInputStream != null)
	            				{
	            					bufferedInputStream.close(); bufferedInputStream = null;
	            				}

	            				if (fileInputStream != null)
	            				{
	            					fileInputStream.close(); fileInputStream = null;
	            				}
	            			}
						}
					}

					rfcRequest.addTable("LT_FILE", attachmentList);
				}
			}

			rfcRequest.addTable("LT_ITEM", itemList);
		}

        return simpleRfcTemplate.response(rfcRequest);
	}
}
