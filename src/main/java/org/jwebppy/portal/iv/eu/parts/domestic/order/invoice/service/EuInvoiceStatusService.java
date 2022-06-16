package org.jwebppy.portal.iv.eu.parts.domestic.order.invoice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jwebppy.config.PortalCacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.eu.parts.domestic.common.service.EuPartsDomesticGeneralService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class EuInvoiceStatusService extends EuPartsDomesticGeneralService
{
	@Cacheable(value = PortalCacheConfig.INVOICE_STATUS, key = "#paramMap", unless="#result == null")
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_INVOICELIST2");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_BSTKD", paramMap.getString("poNo"));		// P.O.No.
		rfcRequest.addField("I_CATEGORY", paramMap.getString("type"));	// Type
		rfcRequest.addField("I_DEALER", paramMap.getCustomerNo());
		rfcRequest.addField("I_FKDAT", paramMap.getString("fromDate"));
		rfcRequest.addField("I_IVNO", paramMap.getString("invoiceNo"));	// Invoice No.
		rfcRequest.addField("I_MATNR", paramMap.getString("partsNo"));	// Parts No
		rfcRequest.addField("I_TKDAT", paramMap.getString("toDate"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_VBELN", paramMap.getString("orderNo"));	// Order No.

		return simpleRfcTemplate.response(rfcRequest);
	}

	public DataList getPdfDownload(DataMap paramMap, ArrayList<String> itemChks)
	{
		String chkCount = paramMap.getString("chkCount");

		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_BO_DOC_PRNT_V2");

		rfcRequest.addStructure("CTL_OPTION", "LANGU", "E");
		rfcRequest.addStructure("CTL_OPTION", "PDF_VIEW", "X");

		List<Map<String, Object>> items = new LinkedList<>();
		String vbeln = null;
		String KSCHL = null;
		String VBTYP = null;

		if("1".equals(chkCount)) {
			vbeln = itemChks.get(0);
			VBTYP = itemChks.get(1);

			//20091106 - Credit인경우 'Y010'으로 변경
            if("M".equals(VBTYP)){  // N에 대해서는 검증필
                KSCHL = "Y008";
            } else if("N".equals(VBTYP) || "O".equals(VBTYP) || "P".equals(VBTYP)){
				KSCHL = "Y010";
            }

			Map<String, Object> itemMap = new HashMap<>();
			itemMap.put("KSCHL", KSCHL);
			itemMap.put("VBELN", vbeln);
			items.add(itemMap);
		}else {
			for(int i=0; i<itemChks.size(); i++) {
				String[] aryItemChks = itemChks.get(i).split(",");

				vbeln = aryItemChks[0];
				VBTYP = aryItemChks[1];

				//20091106 - Credit인경우 'Y010'으로 변경
	            if("M".equals(VBTYP)){  // N에 대해서는 검증필
	                KSCHL = "Y008";
	            } else if("N".equals(VBTYP) || "O".equals(VBTYP) || "P".equals(VBTYP)){
					KSCHL = "Y010";
	            }

				Map<String, Object> itemMap = new HashMap<>();
				itemMap.put("KSCHL", KSCHL);
				itemMap.put("VBELN", vbeln);
				items.add(itemMap);
			}
		}
		rfcRequest.addTable("LT_DONO", items);

		return simpleRfcTemplate.response(rfcRequest).getTable("T_PDF");
	}
}
