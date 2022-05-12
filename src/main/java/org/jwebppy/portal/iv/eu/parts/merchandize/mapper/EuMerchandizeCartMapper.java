package org.jwebppy.portal.iv.eu.parts.merchandize.mapper;

import java.util.List;

import org.jwebppy.portal.iv.eu.parts.merchandize.dto.MerchandizeCartDto;
import org.jwebppy.portal.iv.eu.parts.merchandize.entity.MerchandizeCartEntity;
import org.apache.ibatis.annotations.Mapper;

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
