package org.jwebppy.portal.scm.completes.domestic.order.web;

import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.portal.scm.completes.CompletesErpDataMap;
import org.jwebppy.portal.scm.completes.CompletesGeneralController;
import org.jwebppy.portal.scm.completes.domestic.order.service.OrderCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/scm/completes/domestic/order/create")
public class OrderCreateController extends CompletesGeneralController
{
	@Autowired
	private OrderCreateService orderCreateService;

	@RequestMapping("/order_form")
	@ResponseBody
	public void orderForm(Model model, WebRequest webRequest)
	{
		CompletesErpDataMap erpDateMap = getErpUserInfo();

		DataList dataList = orderCreateService.getModel(erpDateMap);
	}
}
