package org.jwebppy.portal.iv.uk.parts.domestic.common.web;

import java.util.Map;

import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.jwebppy.portal.iv.uk.parts.domestic.common.service.UkDealerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH + "/dealer_info")
public class UkDealerInfoController extends UkPartsDomesticGeneralController
{
	@Autowired
	private UkDealerInfoService dealerInfoService;

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
