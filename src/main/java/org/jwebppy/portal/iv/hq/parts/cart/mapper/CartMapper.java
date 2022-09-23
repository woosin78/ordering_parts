package org.jwebppy.portal.iv.hq.parts.cart.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.hq.parts.cart.entity.CartEntity;

@Mapper
public interface CartMapper
{
	public int insert(CartEntity cart);
	public int updateFgDelete(CartEntity cart);
	public int updateOrderQty(CartEntity cart);
	public List<CartEntity> findCarts(CartEntity cart);
	public int exists(CartEntity cart);
	public int doneOrder(CartEntity cart);
}
