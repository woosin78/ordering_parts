package org.jwebppy.portal.iv.hq.parts.domestic.order.create.web;

import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/create")
public class OrderCreateByInterfaceController extends OrderCreateController
{
	@RequestMapping("/if/{source}")
	public String source(@PathVariable String source, Model model, WebRequest webRequest)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}
}
