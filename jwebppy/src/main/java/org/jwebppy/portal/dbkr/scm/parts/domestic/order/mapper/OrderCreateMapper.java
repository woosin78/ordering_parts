package org.jwebppy.portal.dbkr.scm.parts.domestic.order.mapper;

import java.util.List;

import org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto.OnetimeAddressDto;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto.OrderHistoryHeaderDto;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto.OrderHistoryHeaderEntity;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto.OrderHistoryItemEntity;
import org.jwebppy.portal.dbkr.scm.parts.domestic.order.entity.OnetimeAddressEntity;

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
	public List<OnetimeAddressEntity> findAllOnetimeAddressDuplicationCheck(OnetimeAddressDto onetimeAddress);
	public List<OrderHistoryHeaderEntity> findAllOrderHistoryHeader(OrderHistoryHeaderDto orderHistoryHeader);
}
