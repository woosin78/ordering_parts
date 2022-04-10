package org.jwebppy.portal.iv.hq.parts.domestic.claim.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sapportals.connector.ConnectorException;

@Service
public class ClaimCreateService extends PartsDomesticGeneralService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

    @Value("${file.upload.divk.domestic.parts.claim}")
    private boolean UPLOAD_PATH;

	public RfcResponse getInvoiceList(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_INVOICELIST");

		rfcRequest
			.field()
				.add(new String[][] {
					{"I_BGTYP", "P"},
					{"I_USERID", paramMap.getUsername()}
				})
			.and()
			.structure("LS_SEARCH")
				.add(new String[][] {
					{"RFGSK", "X"},
					{"VBTYP", "C"},
					{"VBELN", paramMap.getString("VBELN")}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	// 반품사유 1,2 취득
	public RfcResponse getComplatintReason(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SD_GET_RETURN_REASON");

		rfcRequest.
			field()
				.add(new String[][] {
					{"I_LANGU", "E"},
					{"I_VKORG", paramMap.getSalesOrg()}
				});

		if (paramMap.isEquals("COMP_RES_CD", "2"))
		{
			rfcRequest.addField("LS_ZZCMPCD", paramMap.getString("complaintReason1"));// Reason1 사유
		}

		return simpleRfcTemplate.response(rfcRequest);
	}

	// 반품생성 화면에서 넘어온 파일을 파일서버 지정위치에 업로드하고 파일서버에 저장한 파일명을 반환한다.
	public String saveTempFile(MultipartFile multipartFile, String fileUploadPath) throws IOException
	{
		String name = getUsername() + "_" + CmDateFormatUtils.now("yyyyMMddHHmmss") + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

		File file = new File(UPLOAD_PATH + name);
		multipartFile.transferTo(file);

		return name;
	}

	// 반품생성 화면에서 넘어온 파일을 파일서버 지정위치에서 삭제하고 파일명을 반환한다.
	public String deleteComplaintCreateFile(String srcFileDir, String[] delFileNames) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int delCnt = 0;
		for (String delFileName : delFileNames)
		{
			String destFileNm = srcFileDir + delFileName;
			File destinationFile = new File(destFileNm);
			boolean isDelete = destinationFile.delete();
			if (isDelete)
			{
				delCnt++;
			} else
			{
				sb.append(destFileNm).append(":");
			}

		}
		if (delCnt == delFileNames.length)
		{
			return "DELETE_OK";
		} else
		{
			if (sb.length() > 0)
			{
				return sb.toString().substring(0, sb.toString().length() - 1);
			} else
			{
				return "";
			}
		}
	}

	// SAP로 반품생성정보를 보낸다.
	//@CacheEvict (value = RedisConfig.ORDER_DISPLAY, allEntries = true)
	public RfcResponse sendComplaintInfoToSAP(HttpServletRequest request, PartsErpDataMap paramMap, String filePath) throws ConnectorException, FileNotFoundException, IOException
	{
		final int BUFFER = 1024; // returnTable row 별로 송신할 파일의 byte size (파일 정보를 1kb씩 끊어서 송신함)
		byte[] buffer = new byte[BUFFER];

        // 화면에서 넘어온 ITEM 정보 Start
		String[] ordernos = request.getParameterValues("orderno");
		String[] reason1s = request.getParameterValues("reason1");
		String[] reason2s = request.getParameterValues("reason2");
		String[] descriptions = request.getParameterValues("description");
		String[] items = request.getParameterValues("item");
		String[] partNos = request.getParameterValues("partno");
		String[] quantities = request.getParameterValues("quantity");
		String[] salesDates = request.getParameterValues("salesDate");
        // 화면에서 넘어온 ITEM 정보 End

        // 화면에서 넘어온 FILE 정보 Start
		String[] fileTrs = request.getParameterValues("fileTr");
		String[] orgFileNms = request.getParameterValues("orgFileNm");
		String[] fileNms = request.getParameterValues("fileNm");
        // 화면에서 넘어온 FILE 정보 End

		RfcRequest rfcRequest = new RfcRequest();
		rfcRequest.setFunctionName("Z_EP_COMPLAIN_SAVE");

		// scalar start
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("BSTNK", request.getParameter("poNo"));
		rfcRequest.addField("I_LANGU", paramMap.getLang());
		rfcRequest.addField("I_KVGR5", paramMap.getCustomerGrp5());
		rfcRequest.addField("I_BGTYP", "P");
		// scalar end

        /* table */
        ArrayList<Object> alInputTable = new ArrayList<>();    // 일반 정보용
        ArrayList<Object> alInputTable2 = new ArrayList<>();   // 파일 정보용

        String tmpItemNo = "";
        for (int i = 0 ; i < reason1s.length; i++)
        {
        	HashMap<String, Object> hmInputTable = new HashMap<>();
            hmInputTable.put("CCHECK","" + (i+1));
            if (i < 9)
            {
                hmInputTable.put("ITEM","0000" + (i+1) + "0");
            } else if ( i >= 9 && i < 99)
            {
                hmInputTable.put("ITEM","000" + (i+1) + "0");
            } else if ( i >= 99 && i < 999)
            {
                hmInputTable.put("ITEM","00" + (i+1) + "0");
            }

        	try
        	{
				tmpItemNo = items[i];
        	} catch (Exception e)
        	{
				tmpItemNo = (String)hmInputTable.get("ITEM");
        	}

        	hmInputTable.put("MATERIAL", partNos[i]);
            hmInputTable.put("QTY", quantities[i]);
            hmInputTable.put("REF_ORD", ordernos[i]);
			hmInputTable.put("REF_ITEM", tmpItemNo);
            hmInputTable.put("REQ_QTY", quantities[i]);
            hmInputTable.put("COMPLAINT", reason1s[i]);
            hmInputTable.put("REASON", reason2s[i]);
            hmInputTable.put("COMPLAINT_DESC", descriptions[i]);
            hmInputTable.put("ZZDEALDT", CmStringUtils.removeIgnoreCase(salesDates[i], "."));

            // 반품 생성 ITEM정보 세팅
            alInputTable.add(hmInputTable);

            // 파일 적재 Start
            for (int j = 0; j < fileTrs.length; j++)
            {
            	if (fileTrs[j].equals(ordernos[i] + "_" + items[i]))	// 현재 행에 첨부된 파일 정보일 경우
            	{
            		String[] fileNameArr = fileNms[j].split(":");		// 첨부된 모든 서버저장파일명을 가져온다.
            		String[] orgFileNameArr = orgFileNms[j].split(":");	// 첨부된 모든 클라이언트 표시 파일명을 가져온다.
            		for (int z = 0; z <fileNameArr.length; z++)
            		{
            			String fileName = fileNameArr[z];
            			try
            			{
            				FileInputStream fis = new FileInputStream(filePath + fileName);
            				//송신 파일 설정 1024byte 씩 끊어서 append 한다.
                            BufferedInputStream in = new BufferedInputStream(fis, BUFFER);
                            int k = 1;
                            while (in.read(buffer) != -1)
                            {
                                HashMap<String, Object> tmpMap = new HashMap<>();

                                tmpMap.put("INT_NO", (z + 1));
                                tmpMap.put("DOCU_ITEM", "0000" + (i + 1) + "0");
                                tmpMap.put("FILE_NAME", orgFileNameArr[z]);
                                tmpMap.put("DESCRIPTION","Complaint_Req");
                                tmpMap.put("FILE_NO",(k++) + "");
                                tmpMap.put("FILE_DATA",buffer);
                                alInputTable2.add(tmpMap);

                                buffer = new byte[BUFFER];
                            }

                            in.close();
            			} finally
            			{

            			}
            		}// 하나의 TR 행에 달린 복수개 파일 적재 End

            	}
            }// 파일 적재 End
        }

		rfcRequest.addTable("LT_ITEM", alInputTable);	// ITEM 정보 적재
		rfcRequest.addTable("LT_FILE", alInputTable2);	// FILE 정보 적재

		RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

		// 반품 생성 요청 대상인 모든 파일을 자체 스토리지에서 삭제(RFC요청 이후에 지우는 타이밍으로 교체할지 여부 확인)
		for (String fileNmBfrSplit : fileNms)
		{
			String[] fileNmForDelArr = fileNmBfrSplit.split(":");
			for (String fileNmForDel : fileNmForDelArr)
			{
				String destFileNm = filePath + fileNmForDel;
				File destinationFile = new File(destFileNm);
				destinationFile.delete();
			}
		}

        return rfcResponse;
	}
}
