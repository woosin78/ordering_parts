package org.jwebppy.portal.iv.hq.parts.domestic.order.create.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.service.OrderCreateGateService;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.service.OrderCreateService;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.util.OrderCreationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/order/create/gate")
public class OrderCreateGateController extends PartsDomesticGeneralController
{
	@Autowired
	private OrderCreateGateService orderCreateGateService;

	@Autowired
	private OrderCreateService orderCreateService;

	//시스뱅크 연동 주문 생성
	@RequestMapping
	public Object sysbank(Model model, WebRequest webRequest)
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

		return new RedirectView(PartsDomesticCommonVo.REQUEST_PATH + "/order/create/write?ohhSeq=" + orderCreateGateService.saveSysbankOrder(paramMap) + "&simulationFrom=" + from);
	}

	@RequestMapping("/{from}")
	public Object gate(@PathVariable("from") String from, @ModelAttribute OrderDto order, WebRequest webRequest)
	{
		String[] materialNos = webRequest.getParameterValues("materialNo");
		String[] orderQties = webRequest.getParameterValues("orderQty");

		if (ArrayUtils.isNotEmpty(materialNos))
		{
			List<OrderItemDto> orderItems = new ArrayList<>();

			for (int i=0, length=materialNos.length; i<length; i++)
			{
				if (CmStringUtils.isEmpty(materialNos[i]))
				{
					continue;
				}

				OrderItemDto orderItem = new OrderItemDto();
				orderItem.setLineNo(OrderCreationUtils.lineNo(i+1));
				orderItem.setMaterialNo(materialNos[i]);

				try
				{
					orderItem.setOrderQty(orderQties[i]);
				}
				catch (NullPointerException | IndexOutOfBoundsException e)
				{
					//orderItem.setOrderQty("1");
				}

				orderItems.add(orderItem);
			}

			order.setRefSystem(from);
			order.setSimulationFrom(CmStringUtils.upperCase(from));
			order.setOrderItems(orderItems);
		}

		return redirectUrl(saveOrderHistory(order));
	}

	protected OrderDto saveOrderHistory(OrderDto order)
	{
		ErpUserContext erpUserContext = getErpUserContext();

		order.setDocType(CmStringUtils.defaultIfEmpty(order.getDocType(), "C"));
		order.setOrderType(CmStringUtils.defaultIfEmpty(order.getOrderType(), "YDSO"));
    	order.setSoldToNo(erpUserContext.getCustCode());
    	order.setShipToNo(erpUserContext.getCustCode());
    	order.setSalesOrg(erpUserContext.getSalesOrg());
    	order.setDistChannel(erpUserContext.getDistChl());
    	order.setDivision(erpUserContext.getDivision());
    	order.setShippingCondition(CmStringUtils.defaultIfEmpty(order.getShippingCondition(), CmStringUtils.defaultIfEmpty(erpUserContext.getShippingCondition(), "-")));//DB 에 not null 조건이므로 임의의 값 저장
    	order.setPoNo(CmStringUtils.defaultIfEmpty(order.getPoNo(), OrderCreationUtils.poNo(order.getSoldToNo())));

    	order.setOhhSeq(orderCreateService.saveOrderHistory(order));

    	return order;
	}

	protected RedirectView redirectUrl(OrderDto order)
	{
		return new RedirectView(PartsDomesticCommonVo.REQUEST_PATH + "/order/create/write?ohhSeq=" + order.getOhhSeq() + "&simulationFrom=" + order.getSimulationFrom());
	}
}
