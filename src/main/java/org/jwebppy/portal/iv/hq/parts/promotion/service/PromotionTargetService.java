package org.jwebppy.portal.iv.hq.parts.promotion.service;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.hq.parts.common.service.PartsGeneralService;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionTargetDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionTargetEntity;
import org.jwebppy.portal.iv.hq.parts.promotion.mapper.PromotionTargetMapper;
import org.jwebppy.portal.iv.hq.parts.promotion.mapper.PromotionTargetObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionTargetService extends PartsGeneralService
{
	@Autowired
	private PromotionTargetMapper promotionTargetMapper;

	/**
	 * promotionTarget DB insert
	 * @param promotionTarget
	 * @return
	 */
	public int save(PromotionTargetDto promotionTarget)
	{
		return promotionTargetMapper.insert(CmModelMapperUtils.mapToEntity(PromotionTargetObjectMapper.INSTANCE, promotionTarget));
	}

	/**
	 * promotionTargets 저장
	 * @param promotionTargets
	 * @return
	 */
	public int save(List<PromotionTargetDto> promotionTargets)
	{
		for (PromotionTargetDto promotionTarget: ListUtils.emptyIfNull(promotionTargets))
		{
			save(promotionTarget);
		}

		return 1;
	}

	/**
	 * 새글 등록 시 리턴된 pSeq 세팅 후 PromotionTarget 저장
	 * @param promotionTargets
	 * @param pSeq
	 * @return
	 */
	public int save(List<PromotionTargetDto> promotionTargets, Integer pSeq)
	{
		for (PromotionTargetDto promotionTarget: ListUtils.emptyIfNull(promotionTargets))
		{
			promotionTarget.setPSeq(pSeq);
			save(promotionTarget);
		}

		return 1;
	}

	public List<PromotionTargetDto> getPromotionTargets(PromotionTargetDto promotionTarget)
	{
		return CmModelMapperUtils.mapToDto(PromotionTargetObjectMapper.INSTANCE, promotionTargetMapper.findPromotionTargets(promotionTarget));
	}

	public int delete(Integer pSeq)
	{
		PromotionTargetEntity promotionTarget = new PromotionTargetEntity();
		promotionTarget.setPSeq(pSeq);
		return promotionTargetMapper.updateFgDelete(promotionTarget);
	}





}
