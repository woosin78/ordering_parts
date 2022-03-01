package org.jwebppy.portal.iv.hq.parts.export.dealer_info.web;

import java.util.Map;

import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.dealer_info.service.DealerInfoService;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/dealer_info")
public class ExDealerInfoController extends PartsDomesticGeneralController
{
	@Autowired
	private DealerInfoService dealerInfoService;

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object viewData(Model model, WebRequest webRequest)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		Map dataMap = dealerInfoService.getDealerInfo(rfcParamMap).getStructure("O_DATA");

		FormatBuilder.with(dataMap)
			.dateFormat("ERDAT");

		return dataMap;
	}
}
