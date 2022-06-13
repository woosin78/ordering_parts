package org.jwebppy.portal.iv.hq.parts.domestic.order.create.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.db.SimpleDauMap;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.service.PartsDomesticGeneralService;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCreateGateService extends PartsDomesticGeneralService
{
	public static void  main(String[] args)
	{
		String a = "1.00";

		System.err.println((int)(Double.parseDouble(a)));
	}

	@Autowired
	private OrderCreateService orderCreateService;

	public int save(Map<String, String> paramMap)
	{
        SimpleDauMap dauMap = null;

        OrderDto order = new OrderDto();

        String customerNo = paramMap.get("customerNo");
        String seq = paramMap.get("seq");

        try
        {
        	dauMap = new SimpleDauMap("SYSBANK", paramMap);//SYSBANK

        	dauMap.query(" select dor_code, dor_dv, dor_type, dor_regdt, dor_urldt, dor_dbzdt, dor_dbzcode, dor_rcvyn from dorder_master_" + customerNo);
        	dauMap.query(" where ");
        	dauMap.query(" dor_code='" + seq + "' ");

        	Map<String, Object> masterMap = dauMap.executeQueryForMap();

        	order.setDocType("C");
        	order.setOrderType(CmStringUtils.trimToEmpty(masterMap.get("dor_type")));
        	order.setSoldToNo(customerNo);
        	order.setShipToNo(customerNo);
        	order.setSalesOrg(paramMap.get("salesOrg"));
        	order.setDistChannel(paramMap.get("distChannel"));
        	order.setDivision(paramMap.get("division"));
        	order.setShippingCondition("-");
        	order.setPoNo(CmStringUtils.stripStart(customerNo, "0") + CmDateFormatUtils.format(CmDateTimeUtils.now(), PortalCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS));
        	order.setRefSeq(CmStringUtils.trimToEmpty(seq));
        	order.setRefSystem(CmStringUtils.trimToEmpty(paramMap.get("from")));

        	dauMap.clearQuery();

        	dauMap.query(" select dor_code, dor_sno, dor_su, dor_regdt from dorder_" + customerNo);
        	dauMap.query(" where ");
        	dauMap.query(" dor_code='" + seq + "' ");

        	List<Map<String, Object>> list = ListUtils.emptyIfNull(dauMap.executeQuery());

        	List<OrderItemDto> orderItems = new ArrayList<>();

        	for (Map<String, Object> map: list)
        	{
        		OrderItemDto orderItem = new OrderItemDto();
        		orderItem.setMaterialNo(CmStringUtils.trimToEmpty(map.get("dor_sno")));
        		orderItem.setOrderQty(Integer.toString((int)(Double.parseDouble(map.get("dor_su").toString()))));

        		orderItems.add(orderItem);
        	}

        	order.setOrderItems(orderItems);

        	int ohhSeq = orderCreateService.saveOrderHistory(order);

        	dauMap.clearQuery();

        	dauMap.query(" update dorder_master_" + customerNo + " set dor_dbzdt=getdate() where dor_code='" + seq + "' ");
        	//dauMap.executeUpdate();

        	return ohhSeq;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { if (dauMap != null) { dauMap.close(); } dauMap = null; } catch (Exception e) {}
        }

		return 0;
	}

	public void done(String poNo, String seq, String customerNo)
	{
		SimpleDauMap dauMap = null;

        try
        {
        	dauMap = new SimpleDauMap("SYSBANK");//SYSBANK

        	dauMap.query(" update dorder_master_" + customerNo + " set dor_dbzcode='" + poNo + "' where dor_code='" + customerNo + "' ");

        	//dauMap.executeUpdate();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { if (dauMap != null) { dauMap.close(); } dauMap = null; } catch (Exception e) {}
        }
	}
}
