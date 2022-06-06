package org.jwebppy.portal.iv.eu.parts.domestic.common.web;

import java.util.Map;

import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.common.service.EuDealerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH + "/dealer_info")
public class EuDealerInfoController extends EuPartsDomesticGeneralController
{
	@Autowired
	private EuDealerInfoService dealerInfoService;

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
		Map dataMap = dealerInfoService.getDealerInfo(getErpUserInfoByUsername()).getStructure("O_DATA");

		FormatBuilder.with(dataMap)
			.dateFormat("ERDAT");

		return dataMap;
	}
}
