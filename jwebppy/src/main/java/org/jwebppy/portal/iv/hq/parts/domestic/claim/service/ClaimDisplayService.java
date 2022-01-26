package org.jwebppy.portal.iv.hq.parts.domestic.claim.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.common.service.PortalGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimDisplayService extends PortalGeneralService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_ORDERLIST");

		rfcRequest.with(paramMap)
			.field()
				.add(new Object[][] {
					{"I_BGTYP", "P"},//상수
					{"I_LANGU", paramMap.getLang()},
					{"I_USERID", paramMap.getUsername()},
					{"COMPLAINT", "Y"}//상수
				})
			.and()
			.structure("LS_SEARCH").with(paramMap)
				.add("VBTYP", "A")
				.addByKey(new Object[][] {
					{"VBELN", "complaintNo"},
					{"VGBEL", "referenceNo"},
					{"MATNR", "partNo"},
					{"FRDATE", "fromDate"},
					{"TODATE", "toDate"}
				})
			.and()
			.output("LT_SEARCH2");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getView(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ORDER_LOAD");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_BGTYP", "P"},//상수
					{"I_LANGU", paramMap.getLang()},
					{"I_USERID", paramMap.getUsername()},
					{"LV_REF_ORD", paramMap.get("orderNo")}
				})
			.and()
			.structure("LS_IMPORT_PORTAL")
				.add("DOC_CATEGORY", paramMap.get("docType"));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public DataList getClaimReasonList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_COMPLAIN_REASON");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_LANG", paramMap.getLang()},
					{"LS_ZZCMPCD", paramMap.get("reason1")}
				})
			.and()
			.output("LT_VALUESET");

		return simpleRfcTemplate.response(rfcRequest).getTable("LT_VALUESET");
	}

	public RfcResponse getDocument(DataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_DOWNLOAD");

		rfcRequest
			.field().with(paramMap)
				.addByKey(new Object[][] {
					{"LV_REF_ORD", "orderNo"},
					{"DOCU_ITEM", "docuItem"},
					{"INT_NO", "intNo"}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getAttachment(DataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_DOWNLOAD");

		rfcRequest
			.field().with(paramMap)
				.addByKey(new Object[][] {
					{"LV_REF_ORD", "orderNo"},
					{"DOCU_ITEM", "docuItem"},
					{"INT_NO", "intNo"}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}
}
