package org.jwebppy.portal.iv.hq.parts.export.order.create.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.util.OrderCreationUtils;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.web.PartsExportGeneralController;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderItemDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.service.ExOrderCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/order/create/gate")
public class ExOrderCreateGateController extends PartsExportGeneralController
{
	@Autowired
	private ExOrderCreateService orderCreateService;

	@RequestMapping("/{from}")
	public Object gate(@PathVariable("from") String from, @ModelAttribute ExOrderDto order, WebRequest webRequest)
	{
		String[] materialNos = webRequest.getParameterValues("materialNo");
		String[] orderQties = webRequest.getParameterValues("orderQty");

		if (ArrayUtils.isNotEmpty(materialNos))
		{
			List<ExOrderItemDto> orderItems = new ArrayList<>();

			for (int i=0, length=materialNos.length; i<length; i++)
			{
				if (CmStringUtils.isEmpty(materialNos[i]))
				{
					continue;
				}

				ExOrderItemDto orderItem = new ExOrderItemDto();
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

	protected ExOrderDto saveOrderHistory(ExOrderDto order)
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

	protected RedirectView redirectUrl(ExOrderDto order)
	{
		return new RedirectView(PartsExportCommonVo.REQUEST_PATH + "/order/create/write?ohhSeq=" + order.getOhhSeq() + "&simulationFrom=" + order.getSimulationFrom());
	}
}
