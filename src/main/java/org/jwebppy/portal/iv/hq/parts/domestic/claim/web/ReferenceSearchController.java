package org.jwebppy.portal.iv.hq.parts.domestic.claim.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.claim.service.ReferenceSearchService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/claim/create/reference")
public class ReferenceSearchController extends PartsDomesticGeneralController
{
	@Autowired
	private ReferenceSearchService referenceSearchService;

	@RequestMapping("/order/popup/list")
	public String list(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/order/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"orderNo", "pOrderNo"},
				{"poNo", "pPoNo"},
				{"partNo", "pPartNo"}
			})
			.addDateByKey(new Object[][] {
				{"fromDate", "pFromDate"},
				{"toDate", "pToDate"}
			});

		RfcResponse rfcResponse = referenceSearchService.getOrderList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("LT_SEARCH2");

		FormatBuilder.with(dataList)
			.decimalFormat(new String[] {"NETPR", "KBETR", "T_NETWR"})
			.qtyFormat("KWMENG")
			.dateFormat("ERDAT");

		return dataList;
	}
}
