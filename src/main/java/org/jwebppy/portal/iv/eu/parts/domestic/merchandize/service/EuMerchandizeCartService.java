package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.EuMerchandizeCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeCartDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeCartEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper.EuMerchandizeCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EuMerchandizeCartService
{
	@Autowired
	private EuMerchandizeCartMapper merchandizeCartMapper;


	// 장바구니 n건 취득
	public List<MerchandizeCartDto> getCartItems(MerchandizeCartDto cartDto) 
	{		
		List<MerchandizeCartEntity> cartEntities = merchandizeCartMapper.findCartItems(cartDto);		
		return CmModelMapperUtils.mapAll(cartEntities, MerchandizeCartDto.class);		
	}
	
	// 장바구니 1건 등록 또는 수정 분기
	@Transactional
	public int saveCart(MerchandizeCartDto dto) 
	{
		int saveCatCnt = 0;
		
		// 등록
		if (EuMerchandizeCommonVo.INSERT.equals(dto.getUpdateFlag())) 
		{
			// 중복체크
			MerchandizeCartEntity checkEntity = merchandizeCartMapper.findCartDuplicateItem(dto);
			if (checkEntity.getMpSeq() == 0) 
			{
				saveCatCnt = merchandizeCartMapper.insertCartItem(CmModelMapperUtils.map(dto, MerchandizeCartEntity.class));
			} else 
			{
				return -1;
			}
		// 수정	
		} else if (EuMerchandizeCommonVo.UPDATE.equals(dto.getUpdateFlag()))
		{
			saveCatCnt = merchandizeCartMapper.updateCartItem(CmModelMapperUtils.map(dto, MerchandizeCartEntity.class));
		}
		
		return saveCatCnt;
	}	
	
	
	// 장바구니 삭제
	@Transactional
	@SuppressWarnings(value={"all"})
	public int deleteCartItem(List<Integer> mcSeqs, List<Integer> mpSeqs, ErpDataMap paramMap) 
	{
		if (CollectionUtils.isNotEmpty(mpSeqs)) 
		{
			int result = 0;
			for (int i = 0; i < mpSeqs.size(); i++) 
			{
				MerchandizeCartEntity cartEntity = new MerchandizeCartEntity();
				cartEntity.setMcSeq(mcSeqs.get(i));
				cartEntity.setMpSeq(mpSeqs.get(i));				
				cartEntity.setCorp(paramMap.getCorpName());
				cartEntity.setRegUsername(paramMap.getUsername());

				result += merchandizeCartMapper.deleteCartItem(cartEntity);								
			}
			return result;
		}
		return 0;
	}
	

	// 장바구니 삽입
	@Transactional
	@SuppressWarnings(value={"all"})
	public int insertCartItem(List<Integer> mpSeqs, List<Integer> quantities, ErpDataMap paramMap) 
	{
		if (CollectionUtils.isNotEmpty(mpSeqs)) 
		{
			int result = 0;
			for (int i = 0; i < mpSeqs.size(); i++) 
			{	
				MerchandizeCartEntity cartEntity = new MerchandizeCartEntity();
				cartEntity.setMpSeq(mpSeqs.get(i));
				
				cartEntity.setOrderQty(CmStringUtils.isEmpty(quantities) ? 1 : quantities.get(i));
				cartEntity.setCorp(paramMap.getCorpName());

				result += merchandizeCartMapper.insertCartItem(cartEntity);								
			}
			return result;
		}
		return 0;
	}

	
	// 장바구니 삽입 또는 수정
	@Transactional
	@SuppressWarnings(value={"all"})
	public int mergeCartItem(List<Integer> mpSeqs, List<Integer> quantities, ErpDataMap paramMap) 
	{
		if (CollectionUtils.isNotEmpty(mpSeqs)) 
		{
			int result = 0;
			for (int i = 0; i < mpSeqs.size(); i++) 
			{	
				MerchandizeCartDto cartDto = new MerchandizeCartDto();				
				cartDto.setMpSeq(mpSeqs.get(i));				
				cartDto.setOrderQty(CmStringUtils.isEmpty(quantities) ? 1 : quantities.get(i));
				cartDto.setCorp(paramMap.getCorpName());
				cartDto.setRegUsername(paramMap.getUsername());

				result += merchandizeCartMapper.mergeCartItem(CmModelMapperUtils.map(cartDto, MerchandizeCartEntity.class));								
			}
			return result;
		}
		return 0;
	}
	
}
