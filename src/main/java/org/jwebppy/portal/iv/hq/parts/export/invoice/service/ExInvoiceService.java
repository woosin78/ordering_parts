package org.jwebppy.portal.iv.hq.parts.export.invoice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.common.utils.SimpleRfcMakeParameterUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExInvoiceService extends PartsExportGeneralService
{
	public RfcResponse getList(PartsErpDataMap paramMap)
	{
		String fromDate = paramMap.getString("fromDate");
		String toDate = paramMap.getString("toDate");

		if (paramMap.isNotEmptyValue("invoiceNo") || paramMap.isNotEmptyValue("poNo") ||paramMap.isNotEmptyValue("orderPartNo"))
		{
			fromDate = "19700101";
			toDate = CmDateFormatUtils.unlimitDate(PortalCommonVo.DEFAULT_DATE_FORMAT_YYYYMMDD);
		}

		RfcRequest rfcRequest = new RfcRequest("Z_SS_SHIPMENT_FOR_INVOICE_HEAD");

		rfcRequest
			.field().with(paramMap)
				.add(new Object[][] {
					{"I_VKORG", paramMap.getSalesOrg()},
					{"I_VTWEG", paramMap.getDistChannel()},
					{"I_SPART", paramMap.getDivision()},
					{"I_KUNAG", paramMap.getCustomerNo()},
					{"I_F_BLDAT", fromDate},
					{"I_T_BLDAT", toDate},
					{"I_MATWA", paramMap.getString("orderPartNo").toUpperCase()}
				})
				.addByKey(new Object[][] {
					{"I_F_ZFCIVNO", "invoiceNo"},
					{"I_BSTNK", "poNo"}
				})
			.and()
    		.structure("I_INPUT")
				.add(SimpleRfcMakeParameterUtils.me(paramMap))
			.output("T_DETAIL");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getView(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SS_SHIPMENT_FOR_INVOICE");

		rfcRequest
			.field().with(paramMap)
			.add(new Object[][] {
				{"I_VKORG", paramMap.getSalesOrg()},
				{"I_VTWEG", paramMap.getDistChannel()},
				{"I_SPART", paramMap.getDivision()},
				{"I_KUNAG", paramMap.getCustomerNo()}
			})
			.addByKey(new Object[][] {
				{"I_F_ZFCIVNO", "invoiceNo"},
				{"I_BSTNK", "poNo"},
				{"I_MATWA", "orderPartNo"}
			})
		.and()
		.structure("I_INPUT")
			.add(SimpleRfcMakeParameterUtils.me(paramMap));

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getBlDownload(PartsErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_BL_ATTCH_FILE_DOWN");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_UNAME", paramMap.getUsername()},
					{"I_ZFCIVNO", paramMap.getString("invoiceNo")}
				});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getDownload(PartsErpDataMap paramMap)
	{
		String[] invoiceNo = (String[])paramMap.get("invoiceNo");
		String[] shipmentNo = (String[])paramMap.get("shipmentNo");

		if (ArrayUtils.isNotEmpty(invoiceNo) && ArrayUtils.isNotEmpty(shipmentNo) && invoiceNo.length == shipmentNo.length)
		{
			List<Map<String, Object>> keyList = new ArrayList<>();

			for (int i=0, length=invoiceNo.length; i<length; i++)
			{
				Map<String, Object> keyMap = new HashMap<>();
				keyMap.put("ZFCIVNO", invoiceNo[i]);
				keyMap.put("ZFDONO", shipmentNo[i]);

				keyList.add(keyMap);
			}

			if (CollectionUtils.isNotEmpty(keyList))
			{
				String mode = paramMap.getString("mode");
				String rfcName = ("EDI".equals(mode)) ? "Z_EP_EDI_PRINT2" : "Z_EP_ORDER_PRINT2";

				RfcRequest rfcRequest = new RfcRequest(rfcName);

				if ("EDI".equals(mode))
				{
					rfcRequest
						.field()
							.add(new Object[][] {
								{"I_USERID", paramMap.getUsername()},
								{"I_FKDAT", paramMap.getString("fromDate")},
								{"I_TKDAT", paramMap.getString("toDate")}
							});
				}
				else
				{
					rfcRequest
						.field()
							.add(new Object[][] {
								{"I_UNAME", paramMap.getUsername()},
								{"I_MODE", mode}
							});
				}

				rfcRequest.addTable("LT_DONO", keyList);

				return simpleRfcTemplate.response(rfcRequest);
			}
		}

		return null;
	}
}
