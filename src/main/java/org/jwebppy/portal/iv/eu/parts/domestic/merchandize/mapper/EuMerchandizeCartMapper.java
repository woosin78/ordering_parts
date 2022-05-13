package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeCartDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeCartEntity;

@Mapper
public interface EuMerchandizeCartMapper
{	
	public List<MerchandizeCartEntity> findCartItems(MerchandizeCartDto cart);
	public MerchandizeCartEntity findCartDuplicateItem(MerchandizeCartDto cart);
	public int insertCartItem(MerchandizeCartEntity cart);
	public int updateCartItem(MerchandizeCartEntity cart);
	public int deleteCartItem(MerchandizeCartEntity cart);
	public int mergeCartItem(MerchandizeCartEntity cart);
}
