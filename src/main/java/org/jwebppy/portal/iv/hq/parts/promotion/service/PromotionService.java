package org.jwebppy.portal.iv.hq.parts.promotion.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.util.XssHandleUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.common.service.PartsGeneralService;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionDto;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionItemDto;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionSearchDto;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionTargetDto;
import org.jwebppy.portal.iv.hq.parts.promotion.entity.PromotionEntity;
import org.jwebppy.portal.iv.hq.parts.promotion.mapper.PromotionMapper;
import org.jwebppy.portal.iv.hq.parts.promotion.mapper.PromotionObjectMapper;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.jwebppy.portal.iv.upload.service.EpUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionService extends PartsGeneralService
{
	@Autowired
	private PromotionMapper promotionMapper;

	@Autowired
	private PromotionTargetService promotionTargetService;

	@Autowired
	private PromotionItemService promotionItemService;

	@Autowired
	private EpUploadFileListService uploadFileListService;

	@Autowired
	private EpUploadFileService uploadFileService;

	public static final String UPLOAD_NAME = "SALES-PROMOTION_HQ";

	/**
	 * 프로모션 목록
	 * @param promotionSearch
	 * @return
	 */
	public List<PromotionDto> getPageablePromotions(PromotionSearchDto promotionSearch)
	{
		return CmModelMapperUtils.mapToDto(PromotionObjectMapper.INSTANCE, promotionMapper.findPageablePromotions(promotionSearch));
	}

	/**
	 * 프로모션 배너 목록
	 * @param promotionSearch
	 * @return
	 */
	public List<PromotionDto> getBannerPromotions(PromotionSearchDto promotionSearch)
	{
		return CmModelMapperUtils.mapToDto(PromotionObjectMapper.INSTANCE, promotionMapper.findBannerPromotions(promotionSearch));
	}

	/**
	 * 프로모션 상세정보
	 * @param pSeq
	 * @return
	 */
	public PromotionDto getPromotion(Integer pSeq)
	{
		return CmModelMapperUtils.mapToDto(PromotionObjectMapper.INSTANCE, promotionMapper.findPromotion(pSeq));
	}

	/**
	 * 프로모션 상세정보
	 * @param promotionSearch
	 * @return
	 */
	public PromotionDto getPromotion(PromotionSearchDto promotionSearch, PartsErpDataMap rfcParamMap)
	{
		PromotionDto promotion = getPromotion(promotionSearch.getPSeq());

		/*
		 * Sales Area check
		 * 예) Target: F11610, F11620, 781610, 721610
		 */
		if (ObjectUtils.isEmpty(promotion) || CmStringUtils.notEquals(promotion.getTarget(), promotionSearch.getTarget()))
		{
			return null;
		}

		//본인 프로모션만 조회해야 할 경우 custCode 에 customer code 값을 넣어준다.
		if (CmStringUtils.isNotEmpty(promotionSearch.getCustCode()))
		{
			List<PromotionTargetDto> promotionTargets = promotion.getPromotionTargets();

			if (CollectionUtils.isNotEmpty(promotion.getPromotionTargets()))
			{
				String custCode = promotionSearch.getCustCode();
				boolean isExists = false;

				for (PromotionTargetDto promotionTarget: promotionTargets)
				{
					if (CmStringUtils.equals(promotionTarget.getCode(), custCode))
					{
						isExists = true;
					}
				}

				if (!isExists)
				{
					return null;
				}
			}
		}

		List<PromotionItemDto> promotionItems = promotion.getPromotionItems();

		if (CollectionUtils.isNotEmpty(promotionItems))
		{
			//부품명 가져와서 데이터 세팅
			String[] materialNos = new String[promotionItems.size()];
			for (int i=0, size=promotionItems.size(); i<size; i++)
			{
				PromotionItemDto pItem = promotionItems.get(i);
				materialNos[i] = pItem.getMaterialNo();
			}
	        rfcParamMap.add("materialNos", materialNos);
	        Map<String, Map<String, Object>> materialInfo = getMaterialInfo(rfcParamMap);

	        for (int i=0, size=promotionItems.size(); i<size; i++)
			{
				PromotionItemDto pItem = promotionItems.get(i);
				pItem.setMaterialText(materialInfo.get(pItem.getMaterialNo()).get("MATERIAL_TEXT").toString());
				promotionItems.set(i, pItem);
			}

	        promotion.setPromotionItems(promotionItems);
		}

		return promotion;
	}

	/**
	 * 프로모션 저장
	 * @param promotion
	 * @return
	 * @throws IOException
	 */
	public int save(PromotionDto promotion) throws IOException
	{
		if (ObjectUtils.isEmpty(promotion.getPSeq()))
		{
			return create(promotion);
		}
		else
		{
			return modify(promotion);
		}
	}

	/**
	 * 프로모션 생성
	 * @param promotion
	 * @return
	 * @throws IOException
	 */
	public int create(PromotionDto promotion) throws IOException
	{
		EpUploadFileDto uploadFile = uploadFileService.getUploadFileByName(UPLOAD_NAME);

		promotion.setUfSeq(uploadFile.getUfSeq());
		promotion.setWriter(UserAuthenticationUtils.getUserDetails().getName());
		promotion.setHtmlContent(XssHandleUtils.removeInvalidTagAndEvent(promotion.getHtmlContent()));
		PromotionEntity promotionEntity = CmModelMapperUtils.mapToEntity(PromotionObjectMapper.INSTANCE, promotion);
		promotionMapper.insert(promotionEntity);

		Integer pSeq = promotionEntity.getPSeq();

		// 배너이미지
		uploadFileListService.save(uploadFile, String.valueOf(pSeq), "B", promotion.getFiles()); // B : Banner, N: Normal
		// 첨부파일
		uploadFileListService.save(uploadFile, String.valueOf(pSeq), "N", promotion.getFiles2());
		// 프로모션 대상
		if (ObjectUtils.isNotEmpty(promotion.getPromotionTargets()))
		{
			promotionTargetService.save(promotion.getPromotionTargets(), pSeq);
		}
		//프로모션 부품
		if (ObjectUtils.isNotEmpty(promotion.getPromotionItems()))
		{
			promotionItemService.save(promotion.getPromotionItems(), pSeq);
		}

		return pSeq;
	}

	/**
	 * 프로모션 수정
	 * @param promotion
	 * @return
	 * @throws IOException
	 */
	public int modify(PromotionDto promotion) throws IOException
	{
		promotion.setHtmlContent(XssHandleUtils.removeInvalidTagAndEvent(promotion.getHtmlContent()));

		if (promotionMapper.update(CmModelMapperUtils.mapToEntity(PromotionObjectMapper.INSTANCE, promotion)) > 0)
		{
			Integer pSeq = promotion.getPSeq();

			uploadFileListService.save(UPLOAD_NAME, String.valueOf(pSeq), "B", promotion.getFiles());
			uploadFileListService.delete(promotion.getUflSeqs());

			uploadFileListService.save(UPLOAD_NAME, String.valueOf(pSeq), "N", promotion.getFiles2());
			uploadFileListService.delete(promotion.getUflSeqs2());

			promotionTargetService.delete(pSeq);
			promotionTargetService.save(promotion.getPromotionTargets());

			promotionItemService.delete(pSeq);
			promotionItemService.save(promotion.getPromotionItems());
		}

		return promotion.getPSeq();
	}

	/**
	 * 프로모션 삭제
	 * @param promotion
	 * @return
	 */
	public int delete(PromotionDto promotion)
	{
		Integer pSeq = promotion.getPSeq();
		if (ObjectUtils.isEmpty(pSeq))
		{
			return 0;
		}

		if (0 < promotionMapper.updateFgDelete(CmModelMapperUtils.mapToEntity(PromotionObjectMapper.INSTANCE, promotion)))
		{
			uploadFileListService.delete(promotion.getUflSeqs());
			uploadFileListService.delete(promotion.getUflSeqs2());
			promotionTargetService.delete(pSeq);
			promotionItemService.delete(pSeq);
			return 1;
		}
		return 0;
	}

	/**
	 * 부품정보 가져오기
	 * @param paramMap
	 * @return
	 */
	public Map<String, Map<String, Object>> getMaterialInfo(PartsErpDataMap paramMap)
    {

		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_QTY");

		rfcRequest.
			field()
				.add(new Object[][] {
					{"I_LANGU", paramMap.getLangForSap()},
					{"I_BGTYP", "P"},
					{"I_USERID", paramMap.getUsername()}
				});

		List<Map<String, Object>> items = new LinkedList<>();

		String[] matertialNos = (String[])paramMap.get("materialNos");

		int index = 1;
		for (String material: matertialNos)
		{
			if (CmStringUtils.isEmpty(material))
			{
				continue;
			}

			Map<String, Object> itemMap = new HashMap<>();
			itemMap.put("ITEM", index++);
			itemMap.put("MATERIAL", material);

			items.add(itemMap);
		}

		rfcRequest.addTable("LT_ITEM", items);

		DataList ltItems = simpleRfcTemplate.response(rfcRequest).getTable("LT_ITEM");

		if (CollectionUtils.isNotEmpty(ltItems))
		{
			Map<String, Map<String, Object>> resultMap = new HashMap<>();

			for (int i=0, size=ltItems.size(); i<size; i++)
			{
				Map<String, Object> itemMap = (Map<String, Object>)ltItems.get(i);

				resultMap.put(CmStringUtils.trimToEmpty(itemMap.get("MATERIAL")), itemMap);
			}

			return resultMap;
		}

		return Collections.emptyMap() ;
    }
}
