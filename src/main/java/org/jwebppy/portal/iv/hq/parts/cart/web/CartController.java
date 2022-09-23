package org.jwebppy.portal.iv.hq.parts.cart.web;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.cart.dto.CartDto;
import org.jwebppy.portal.iv.hq.parts.cart.service.CartService;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.web.PartsGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsCommonVo.REQUEST_PATH + "/cart")
public class CartController extends PartsGeneralController
{
	@Autowired
	private CartService cartService;

	@PostMapping("/add")
	@ResponseBody
	public Object add(CartDto cart)
	{
		cart.setUSeq(getUSeq());
		return cartService.add(cart);
	}

	@PostMapping("/update/order_qty")
	@ResponseBody
	public Object updateOrderQty(CartDto cart)
	{
		return cartService.updateOrderQty(cart);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(CartDto cart)
	{
		return cartService.delete(cart);
	}

	@RequestMapping("/popup/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("orderUrl", getOrderUrl());

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute CartDto cart, WebRequest webRequest)
	{
		cart.setUSeq(getUSeq());
		return ListUtils.emptyIfNull(cartService.getCarts(cart, getErpUserInfo()));
	}

	private String getOrderUrl()
	{
		ErpUserContext erpUserContext = getErpUserContext();

		String salesOrg = erpUserContext.getSalesOrg();
		String corp = "hq";

		if (CmStringUtils.equals(salesOrg, "7816"))
		{
			corp = "eu";
		}
		else if (CmStringUtils.equals(salesOrg, "7216"))
		{
			corp = "uk";
		}

		String channel = (CmStringUtils.equals(erpUserContext.getDistChl(), "10")) ? "domestic": "export";

		return "/portal/iv/" + corp + "/parts/" + channel + "/order/create/gate/cart";
	}
}
