package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeProductSearchDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeRecommendProductDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeProductEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeRecommendProductEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper.EuMerchandizeRecommendProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EuMerchandizeRecommendProductService
{
	@Autowired
	private EuMerchandizeRecommendProductMapper merchandizeRecommendProductMapper;

	
	// 추천상품 n건 취득(메인 화면에서 사용)
	public List<MerchandizeRecommendProductDto> getRecommendProductItemsMain(MerchandizeRecommendProductDto dto) 
	{		
		List<MerchandizeRecommendProductEntity> productEntities = merchandizeRecommendProductMapper.findRecommendProductItemsMain(dto);		
		return CmModelMapperUtils.mapAll(productEntities, MerchandizeRecommendProductDto.class);		
	}
	

	// 추천상품 정렬번호 갯수 취득(메인 화면에서 사용)
	public MerchandizeRecommendProductDto getRecommendProductItemsMainCount(MerchandizeRecommendProductDto recommendProductDto) 
	{
		MerchandizeRecommendProductEntity recommendProductEntity = merchandizeRecommendProductMapper.findRecommendProductItemsMainCount(recommendProductDto);		
		return CmModelMapperUtils.map(recommendProductEntity, MerchandizeRecommendProductDto.class);		
	}
	
	
	// 추천상품 n건 취득(팝업에서 검색하는 리스트)
	public List<MerchandizeProductDto> getRecommendProductItems(MerchandizeProductSearchDto productSearchDto) 
	{		
		List<MerchandizeProductEntity> productEntities = merchandizeRecommendProductMapper.findRecommendProductItems(productSearchDto);		
		return CmModelMapperUtils.mapAll(productEntities, MerchandizeProductDto.class);		
	}
	
	
	// 상품 1건 등록 또는 수정 분기
	@Transactional
	public int saveRecommendProduct(MerchandizeRecommendProductDto dto) 
	{
		int savePrdCnt = 0;
		
//		// 수정
//		if (CmStringUtils.isNotEmpty(dto.getMpSeq()))	 
//		{			
//			MerchandizeProductEntity updateEntity = CmModelMapperUtils.map(dto, MerchandizeProductEntity.class);
//			savePrdCnt = merchandizeProductMapper.updateProductItem(updateEntity);
//			saveProductLangData(dto, updateEntity, MerchandizeCommonVo.UPDATE);
//			
//			return savePrdCnt;
//			
//		// 등록	
//		} else 
//		{	
//			// 상품코드 중복 체크
//			MerchandizeProductEntity productEntity = merchandizeProductMapper.verifyProductItem(dto);			
//			if (productEntity == null || CmStringUtils.isEmpty(productEntity.getMaterialNo())) 
//			{
//				MerchandizeProductEntity insertEntity = CmModelMapperUtils.map(dto, MerchandizeProductEntity.class);								
//				savePrdCnt = merchandizeProductMapper.insertProductItem(insertEntity);				
//				saveProductLangData(dto, insertEntity, MerchandizeCommonVo.INSERT);
//				
//				return savePrdCnt;
//			} else 
//			{
//				return -1;	// 중복
//			}
//		}				
		
		return savePrdCnt;
	}	
	

	// 상품 1건 등록
	public int insertRecommendProductItem(MerchandizeRecommendProductEntity reProductEntity) 
	{
		return merchandizeRecommendProductMapper.insertRecommendProductItem(reProductEntity);
	}
		
	// 상품 1건 수정
	public int modifyRecommendProductItem(MerchandizeRecommendProductEntity reProductEntity) 
	{
		return merchandizeRecommendProductMapper.updateRecommendProductItem(reProductEntity);
	}	
	

	// 추천상품 등록
	@Transactional
	public int insertRecommendProducts(List<Integer> seqs) 
	{
		if (CollectionUtils.isNotEmpty(seqs)) 
		{
			int result = 0;
			for (Integer seq : seqs) 
			{
				MerchandizeRecommendProductEntity reProductEntity = new MerchandizeRecommendProductEntity();
				reProductEntity.setMpSeq(seq);
				reProductEntity.setFgDelete(PlatformCommonVo.NO);

				// 추천상품 테이블(ep_mall_recommend_product) 데이터 merge 처리
				result += merchandizeRecommendProductMapper.insertRecommendProductItem(reProductEntity);				
			}
			return result;
		}
		return 0;
	}
	
	// 상품 삭제(삭제 플래그만 세움)
	@Transactional
	public int deleteRecommendProducts(List<Integer> seqs) 
	{
		if (CollectionUtils.isNotEmpty(seqs)) 
		{
			int result = 0;
			for (Integer seq : seqs) 
			{
				MerchandizeRecommendProductEntity reProductEntity = new MerchandizeRecommendProductEntity();
				reProductEntity.setMrpSeq(seq);
				reProductEntity.setFgDelete(PlatformCommonVo.YES);

				// 추천상품 테이블(ep_mall_recommend_product) 데이터 삭제플래그 처리
				result += merchandizeRecommendProductMapper.updateRecommendProductItem(reProductEntity);				
			}
			return result;
		}
		return 0;
	}
	

	// 추천상품 갯수 체크
	public MerchandizeRecommendProductDto checkRecommendProductItem(MerchandizeRecommendProductDto recommendProductDto) 
	{
		MerchandizeRecommendProductEntity recommendProductEntity = merchandizeRecommendProductMapper.checkRecommendProductItem(recommendProductDto);		
		return CmModelMapperUtils.map(recommendProductEntity, MerchandizeRecommendProductDto.class);		
	}
}
