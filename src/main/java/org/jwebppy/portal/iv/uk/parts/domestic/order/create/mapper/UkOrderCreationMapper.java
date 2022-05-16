package org.jwebppy.portal.iv.uk.parts.domestic.order.create.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOnetimeAddressDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderHistoryHeaderDto;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity.UkOnetimeAddressEntity;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity.UkOrderHistoryHeaderEntity;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity.UkOrderHistoryItemEntity;

@Mapper
public interface UkOrderCreationMapper
{
	public int insertOnetimeAddress(UkOnetimeAddressEntity onetimeAddress);
	public int insertOrderHistoryHeader(UkOrderHistoryHeaderEntity orderHistoryHeader);
	public int insertOrderHistoryItem(UkOrderHistoryItemEntity orderHistoryItem);
	public int updateFgUseOfOnetimeAddress(UkOnetimeAddressEntity onetimeAddress);
	public int updateFgDeleteOfOnetimeAddress(UkOnetimeAddressEntity onetimeAddress);
	public int updateSuccessOrderHistoryHeader(UkOrderHistoryHeaderEntity orderHistoryHeader);
	public int updateFailOrderHistoryHeader(UkOrderHistoryHeaderEntity orderHistoryHeader);
	public UkOnetimeAddressEntity findOnetimeAddressByOaSeq(Integer oaSeq);
	public List<UkOnetimeAddressEntity> findAllOnetimeAddresses(UkOnetimeAddressDto onetimeAddress);
	public List<UkOnetimeAddressEntity> findAllOnetimeAddressDuplicationCheck(UkOnetimeAddressDto onetimeAddress);
	public List<UkOrderHistoryHeaderEntity> findAllOrderHistoryHeader(UkOrderHistoryHeaderDto orderHistoryHeader);
}
