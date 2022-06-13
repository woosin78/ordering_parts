package org.jwebppy.portal.iv.hq.parts.domestic.order.create.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.service.OrderCreateGateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/create/gate")
public class OrderCreateGateController extends PartsDomesticGeneralController
{
	@Autowired
	private OrderCreateGateService orderCreateGateService;

	@RequestMapping
	public Object gate(Model model, WebRequest webRequest)
	{
		String from = webRequest.getParameter("from");
		String seq = webRequest.getParameter("seq");

		ErpUserContext erpUserContext = getErpUserContext();

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("seq", seq);
		paramMap.put("from", from);
		paramMap.put("customerNo", erpUserContext.getCustCode());
		paramMap.put("salesOrg", erpUserContext.getSalesOrg());
		paramMap.put("distChannel", erpUserContext.getDistChl());
		paramMap.put("division", erpUserContext.getDivision());

		return new RedirectView(PartsDomesticCommonVo.REQUEST_PATH + "/order/create/write?ohhSeq=" + orderCreateGateService.save(paramMap));
	}
}
