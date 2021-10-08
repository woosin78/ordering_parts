package org.jwebppy.portal.scm.parts.domestic.order.create.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.scm.parts.domestic.order.create.dto.OnetimeAddressDto;
import org.jwebppy.portal.scm.parts.domestic.order.create.dto.OrderHistoryHeaderDto;
import org.jwebppy.portal.scm.parts.domestic.order.create.entity.OnetimeAddressEntity;
import org.jwebppy.portal.scm.parts.domestic.order.create.entity.OrderHistoryHeaderEntity;
import org.jwebppy.portal.scm.parts.domestic.order.create.entity.OrderHistoryItemEntity;

@Mapper
public interface OrderCreateMapper
{
	public int insertOnetimeAddress(OnetimeAddressEntity onetimeAddress);
	public int insertOrderHistoryHeader(OrderHistoryHeaderEntity orderHistoryHeader);
	public int insertOrderHistoryItem(OrderHistoryItemEntity orderHistoryItem);
	public int updateFgUseOfOnetimeAddress(OnetimeAddressEntity onetimeAddress);
	public int updateFgDeleteOfOnetimeAddress(OnetimeAddressEntity onetimeAddress);
	public int updateSuccessOrderHistoryHeader(OrderHistoryHeaderEntity orderHistoryHeader);
	public int updateFailOrderHistoryHeader(OrderHistoryHeaderEntity orderHistoryHeader);
	public OnetimeAddressEntity findOnetimeAddressByOaSeq(Integer oaSeq);
	public List<OnetimeAddressEntity> findAllOnetimeAddresses(OnetimeAddressDto onetimeAddress);
	public List<OnetimeAddressEntity> findAllSameOnetimeAddresses(OnetimeAddressDto onetimeAddress);
	public List<OrderHistoryHeaderEntity> findAllOrderHistoryHeader(OrderHistoryHeaderDto orderHistoryHeader);
}
