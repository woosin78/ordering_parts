package org.jwebppy.portal.iv.hq.parts.promotion.service;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.hq.parts.common.service.PartsGeneralService;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionItemDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionItemEntity;
import org.jwebppy.portal.iv.hq.parts.promotion.mapper.PromotionItemMapper;
import org.jwebppy.portal.iv.hq.parts.promotion.mapper.PromotionItemObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionItemService extends PartsGeneralService
{
	@Autowired
	private PromotionItemMapper promotionItemMapper;

	/**
	 * promotionItem DB insert
	 * @param promotionItem
	 * @return
	 */
	public int save(PromotionItemDto promotionItem)
	{
		return promotionItemMapper.insert(CmModelMapperUtils.mapToEntity(PromotionItemObjectMapper.INSTANCE, promotionItem));
	}

	/**
	 * promotionItems 저장
	 * @param promotionItems
	 * @return
	 */
	public int save(List<PromotionItemDto> promotionItems)
	{
		for (PromotionItemDto promotionItem: ListUtils.emptyIfNull(promotionItems))
		{
			save(promotionItem);
		}

		return 1;
	}

	/**
	 * 새글 등록 시 리턴된 pSeq 세팅 후 PromotionItem 저장
	 * @param promotionItems
	 * @param pSeq
	 * @return
	 */
	public int save(List<PromotionItemDto> promotionItems, Integer pSeq)
	{
		for (PromotionItemDto promotionItem: ListUtils.emptyIfNull(promotionItems))
		{
			promotionItem.setPSeq(pSeq);
			save(promotionItem);
		}

		return 1;
	}

	public List<PromotionItemDto> getPromotionItems(PromotionItemDto promotionItem)
	{
		return CmModelMapperUtils.mapToDto(PromotionItemObjectMapper.INSTANCE, promotionItemMapper.findPromotionItems(promotionItem));
	}

	public int delete(Integer pSeq)
	{
		PromotionItemEntity promotionItem = new PromotionItemEntity();
		promotionItem.setPSeq(pSeq);
		return promotionItemMapper.updateFgDelete(promotionItem);
	}





}
