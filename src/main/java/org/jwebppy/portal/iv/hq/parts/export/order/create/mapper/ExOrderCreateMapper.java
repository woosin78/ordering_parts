package org.jwebppy.portal.iv.hq.parts.export.order.create.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.hq.parts.export.order.create.dto.ExOrderHistoryHeaderDto;
import org.jwebppy.portal.iv.hq.parts.export.order.create.entity.ExOrderHistoryHeaderEntity;
import org.jwebppy.portal.iv.hq.parts.export.order.create.entity.ExOrderHistoryItemEntity;

@Mapper
public interface ExOrderCreateMapper
{
	public int insertOrderHistoryHeader(ExOrderHistoryHeaderEntity orderHistoryHeader);
	public int insertOrderHistoryItem(ExOrderHistoryItemEntity orderHistoryItem);
	public int updateSuccessOrderHistoryHeader(ExOrderHistoryHeaderEntity orderHistoryHeader);
	public int updateFailOrderHistoryHeader(ExOrderHistoryHeaderEntity orderHistoryHeader);
	public ExOrderHistoryHeaderEntity findOrderHistoryHeader(Integer ohhSeq);
	public List<ExOrderHistoryHeaderEntity> findAllOrderHistoryHeader(ExOrderHistoryHeaderDto orderHistoryHeader);
}
