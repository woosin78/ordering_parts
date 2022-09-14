package org.jwebppy.portal.iv.hq.parts.cart.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.hq.parts.cart.dto.CartDto;
import org.jwebppy.portal.iv.hq.parts.cart.entity.CartEntity;

@Mapper
public interface CartMapper
{
	public int insert(CartEntity cartEntity);
	public int updateFgDelete(CartEntity cartEntity);
	public int existCart(CartEntity cartEntity);
	public List<CartEntity> findCarts(CartEntity cartEntity);
}
