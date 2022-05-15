package org.jwebppy.portal.iv.hq.parts.domestic.order.create.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto.OrderHistoryHeaderDto;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.entity.OrderHistoryHeaderEntity;
import org.jwebppy.portal.iv.hq.parts.domestic.order.create.entity.OrderHistoryItemEntity;

@Mapper
public interface OrderCreateMapper
{
	public int insertOrderHistoryHeader(OrderHistoryHeaderEntity orderHistoryHeader);
	public int insertOrderHistoryItem(OrderHistoryItemEntity orderHistoryItem);
	public int updateSuccessOrderHistoryHeader(OrderHistoryHeaderEntity orderHistoryHeader);
	public int updateFailOrderHistoryHeader(OrderHistoryHeaderEntity orderHistoryHeader);
	public List<OrderHistoryHeaderEntity> findAllOrderHistoryHeader(OrderHistoryHeaderDto orderHistoryHeader);
}
