package org.jwebppy.portal.iv.hq.parts.cart.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.cart.dto.CartDto;
import org.jwebppy.portal.iv.hq.parts.cart.entity.CartEntity;
import org.jwebppy.portal.iv.hq.parts.cart.mapper.CartMapper;
import org.jwebppy.portal.iv.hq.parts.cart.mapper.CartObjectMapper;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.common.service.PartsGeneralService;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionItemDto;
import org.jwebppy.portal.iv.hq.parts.promotion.mapper.PromotionObjectMapper;
import org.jwebppy.portal.iv.hq.parts.promotion.service.PromotionService;
import org.jwebppy.portal.iv.hq.parts.promotion.service.PromotionTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService extends PartsGeneralService
{
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private PromotionService promotionService;

	/**
	 * 장바구니 담기
	 * @param cart
	 * @return
	 */
	public int add(CartDto cart) {
		if(CollectionUtils.isEmpty(cart.getMaterialNos()) || cart.getMaterialNos().size() == 0) 
		{
			return insert(cart);
		}
		
		for(int i=0; i<cart.getMaterialNos().size(); i++) 
		{
			cart.setMaterialNo(cart.getMaterialNos().get(i));
			insert(cart);
		}
		
		return 1;
	}

	/**
	 * 장바구니 담기
	 * @param cart
	 * @return
	 */
	public int insert(CartDto cart) {
		if(CmStringUtils.isEmpty(cart.getMaterialNo())) 
		{
			return 0;
		}

		// 이미 있는지 체크
		if( 0 < cartMapper.existCart(CmModelMapperUtils.mapToEntity(CartObjectMapper.INSTANCE, cart))) 
		{
			return 1;
		}
		
		
		if(CmStringUtils.isEmpty(cart.getOrderQty())) 
		{
			cart.setOrderQty("1");
		}
		
		CartEntity cartEntity = CmModelMapperUtils.mapToEntity(CartObjectMapper.INSTANCE, cart);
		cartMapper.insert(cartEntity);
		
		return cartEntity.getCiSeq();
	}
	
	
	/**
	 * 장바구니 삭제
	 * @param cart
	 * @return
	 */
	public int delete(CartDto cart) {
		
		if(CollectionUtils.isEmpty(cart.getCiSeqs()))
		{
			return 0;
		}
		
		for(int i=0; i<cart.getCiSeqs().size(); i++)
		{
			cart.setCiSeq(cart.getCiSeqs().get(i));
			cartMapper.updateFgDelete(CmModelMapperUtils.mapToEntity(CartObjectMapper.INSTANCE, cart));
		}
		
		return 1;
	}

	/**
	 * 장바구니 목록
	 * @param cart
	 * @param rfcParamMap
	 * @return
	 */
	public List<CartDto> getCarts(CartDto cart, PartsErpDataMap rfcParamMap) {
		
		List<CartDto> carts = CmModelMapperUtils.mapToDto(CartObjectMapper.INSTANCE, cartMapper.findCarts(CmModelMapperUtils.mapToEntity(CartObjectMapper.INSTANCE, cart)));
		
		if (CollectionUtils.isNotEmpty(carts))
		{
			//부품명 가져와서 데이터 세팅
			String[] materialNos = new String[carts.size()];
			for (int i=0; i<carts.size(); i++)
			{
				materialNos[i] = carts.get(i).getMaterialNo();
			}
			
			rfcParamMap.add("materialNos", materialNos);
	        Map<String, Map<String, Object>> materialInfo = promotionService.getMaterialInfo(rfcParamMap);
	        
	        for (int i=0; i<carts.size(); i++)
			{
	        	CartDto c = carts.get(i);
	        	c.setMaterialText(materialInfo.get(c.getMaterialNo()).get("MATERIAL_TEXT").toString());
				carts.set(i, c);
			}
		}
		
		return carts;
	}

}
