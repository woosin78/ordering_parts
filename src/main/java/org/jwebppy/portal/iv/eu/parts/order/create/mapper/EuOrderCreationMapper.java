package org.jwebppy.portal.iv.eu.parts.order.create.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.parts.order.create.dto.EuOnetimeAddressDto;
import org.jwebppy.portal.iv.eu.parts.order.create.dto.EuOrderHistoryHeaderDto;
import org.jwebppy.portal.iv.eu.parts.order.create.entity.EuOnetimeAddressEntity;
import org.jwebppy.portal.iv.eu.parts.order.create.entity.EuOrderHistoryHeaderEntity;
import org.jwebppy.portal.iv.eu.parts.order.create.entity.EuOrderHistoryItemEntity;

@Mapper
public interface EuOrderCreationMapper
{
	public int insertOnetimeAddress(EuOnetimeAddressEntity onetimeAddress);
	public int insertOrderHistoryHeader(EuOrderHistoryHeaderEntity orderHistoryHeader);
	public int insertOrderHistoryItem(EuOrderHistoryItemEntity orderHistoryItem);
	public int updateFgUseOfOnetimeAddress(EuOnetimeAddressEntity onetimeAddress);
	public int updateFgDeleteOfOnetimeAddress(EuOnetimeAddressEntity onetimeAddress);
	public int updateSuccessOrderHistoryHeader(EuOrderHistoryHeaderEntity orderHistoryHeader);
	public int updateFailOrderHistoryHeader(EuOrderHistoryHeaderEntity orderHistoryHeader);
	public EuOnetimeAddressEntity findOnetimeAddressByOaSeq(Integer oaSeq);
	public List<EuOnetimeAddressEntity> findAllOnetimeAddresses(EuOnetimeAddressDto onetimeAddress);
	public List<EuOnetimeAddressEntity> findAllOnetimeAddressDuplicationCheck(EuOnetimeAddressDto onetimeAddress);
	public List<EuOrderHistoryHeaderEntity> findAllOrderHistoryHeader(EuOrderHistoryHeaderDto orderHistoryHeader);
}
