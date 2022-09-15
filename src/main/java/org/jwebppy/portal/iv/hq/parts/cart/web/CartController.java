package org.jwebppy.portal.iv.hq.parts.cart.web;

import org.apache.commons.collections4.ListUtils;
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

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(CartDto cart)
	{
		return cartService.delete(cart);
	}

	@RequestMapping("/popup/list")
	public String list(Model model, WebRequest webRequest)
	{
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

	// 주문 후 장바구니 내용 삭제? > 주문을 햇는지 안햇는지 모름
	// 수량 수정시 마다 업데이트?


}
