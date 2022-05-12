package org.jwebppy.portal.iv.eu.parts.merchandize.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.dto.LangType;
import org.jwebppy.platform.mgmt.i18n.entity.LangDetailEntity;
import org.jwebppy.platform.mgmt.i18n.entity.LangEntity;
import org.jwebppy.platform.mgmt.i18n.entity.LangKindEntity;
import org.jwebppy.platform.mgmt.i18n.mapper.LangMapper;
import org.jwebppy.portal.iv.eu.parts.merchandize.EuMerchandizeCommonVo;
import org.jwebppy.portal.iv.eu.parts.merchandize.EuMerchandizeGeneralService;
import org.jwebppy.portal.iv.eu.parts.merchandize.dto.MerchandizeCategoryDto;
import org.jwebppy.portal.iv.eu.parts.merchandize.entity.MerchandizeCategoryEntity;
import org.jwebppy.portal.iv.eu.parts.merchandize.entity.MerchandizeLangRlEntity;
import org.jwebppy.portal.iv.eu.parts.merchandize.mapper.EuMerchandizeCategoryMapper;
import org.jwebppy.portal.iv.eu.parts.merchandize.mapper.EuMerchandizeLangRlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EuMerchandizeCategoryService
{
	@Autowired
	private EuMerchandizeCategoryMapper merchandizeCategoryMapper;

	@Autowired
	private EuMerchandizeLangRlMapper merchandizeLangRlMapper;

	@Autowired
	private LangMapper langMapper;


	// 카테고리 n건 취득
	public List<MerchandizeCategoryDto> getCategorys(MerchandizeCategoryDto categoryDto)
	{
		List<MerchandizeCategoryEntity> categoryEntities = merchandizeCategoryMapper.findCategoryItems(categoryDto);
		return CmModelMapperUtils.mapAll(categoryEntities, MerchandizeCategoryDto.class);
	}

	// 카테고리 정렬번호 갯수 취득
	public MerchandizeCategoryDto getCategoryItemsMainCount(MerchandizeCategoryDto categoryDto)
	{
		MerchandizeCategoryEntity categoryEntity = merchandizeCategoryMapper.findCategoryItemsMainCount(categoryDto);
		return CmModelMapperUtils.map(categoryEntity, MerchandizeCategoryDto.class);
	}

	// 카테고리 1건 취득
	public MerchandizeCategoryDto getCategory(MerchandizeCategoryDto categoryDto)
	{
		MerchandizeCategoryEntity categoryEntity = merchandizeCategoryMapper.findCategoryItem(categoryDto);
		return CmModelMapperUtils.map(categoryEntity, MerchandizeCategoryDto.class);
	}

	// 등록 화면 이동 시 지원하는 언어 리스트 취득
	public List<LangKindEntity> getSupportLangs(LangKindEntity entity)
	{
		return merchandizeCategoryMapper.findSupportLangs(entity);
	}

	// 등록 시 sort number 자동입력을 위한 새로운 sort number 취득
	public MerchandizeCategoryDto getCategoryNewSortNumber(MerchandizeCategoryDto categoryDto)
	{
		MerchandizeCategoryEntity categoryEntity = merchandizeCategoryMapper.findCategoryNewSortNumber(categoryDto);

		return CmModelMapperUtils.map(categoryEntity, MerchandizeCategoryDto.class);
	}

	// 카테고리 1건 등록 또는 수정 분기
	@Transactional
	public int saveCategory(MerchandizeCategoryDto dto)
	{
		int saveCatCnt = 0;

		// 수정
		if (CmStringUtils.isNotEmpty(dto.getMcSeq()))
		{
			MerchandizeCategoryEntity updateEntity = CmModelMapperUtils.map(dto, MerchandizeCategoryEntity.class);
			saveCatCnt = merchandizeCategoryMapper.updateCategoryItem(updateEntity);
			saveCategoryLangData(dto, updateEntity, EuMerchandizeCommonVo.UPDATE);

			return saveCatCnt;

		// 등록
		} else
		{
			MerchandizeCategoryEntity checkEntity = merchandizeCategoryMapper.findCategoryDuplicateCode(dto);
			if (checkEntity == null || checkEntity.getCategoryCode() == null)
			{
				MerchandizeCategoryEntity insertEntity = CmModelMapperUtils.map(dto, MerchandizeCategoryEntity.class);

				saveCatCnt = merchandizeCategoryMapper.insertCategoryItem(insertEntity);

				saveCategoryLangData(dto, insertEntity, EuMerchandizeCommonVo.INSERT);

				return saveCatCnt;
			} else
			{
				return -1;	// 중복
			}
		}
	}

	private void saveCategoryLangData(MerchandizeCategoryDto dto, MerchandizeCategoryEntity categoryEntity, String insertOrUpdate)
	{
		if (EuMerchandizeCommonVo.INSERT.equals(insertOrUpdate))
		{
			insertLangInfo(dto, categoryEntity, new Boolean(false), 0);
		} else if (EuMerchandizeCommonVo.UPDATE.equals(insertOrUpdate))
		{
			for (int i = 0; i < dto.getCategoryLangKindSeqs().length; i++)
			{
				// 다국어 상세 식별자가 0보다 클 경우 정상 갱신, 0일 경우 상품이 추가된 다음에 지원 언어 자체가 추가된 케이스이므로 신규 삽입 처리
				if ( dto.getCategoryNameLangDetailSeqs()[i] > 0 )
				{
					LangDetailEntity ldEntity = new LangDetailEntity();
					ldEntity.setLdSeq(dto.getCategoryNameLangDetailSeqs()[i]);
					ldEntity.setText(dto.getCategoryNames()[i]);
					langMapper.updateLangDetail(ldEntity);		// 다국어 상세 저장
				} else if ( dto.getCategoryNameLangDetailSeqs()[i] == 0 )
				{
					insertLangInfo(dto, categoryEntity, new Boolean(true), i);
				}
			}
		}
	}

	// 카테고리 삭제(삭제 플래그만 세움)
	@Transactional
	public int deleteCategory(List<Integer> seqs)
	{
		if (CollectionUtils.isNotEmpty(seqs))
		{
			int result = 0;
			for (Integer seq : seqs)
			{
				// 카테고리 테이블 데이터 삭제플래그 처리
				MerchandizeCategoryEntity categoryEntity = new MerchandizeCategoryEntity();
				categoryEntity.setMcSeq(seq);
				categoryEntity.setFgDelete(PlatformCommonVo.YES);
				result += merchandizeCategoryMapper.updateCategoryItem(categoryEntity);

				MerchandizeLangRlEntity mlrEntity = new MerchandizeLangRlEntity();
				mlrEntity.setTSeq(categoryEntity.getMcSeq());
				mlrEntity.setType(EuMerchandizeCommonVo.CATEGORY_NAME);
				List<MerchandizeLangRlEntity> deleteMlrEntities = merchandizeLangRlMapper.findLangDetailSeqsForDelete(mlrEntity);

				for (MerchandizeLangRlEntity mlrEntityDel : deleteMlrEntities)
				{
					// 다국어 상세 테이블(pltf_lang_detail) 데이터 삭제플래그 처리
					merchandizeLangRlMapper.deleteLangDetail(mlrEntityDel);

					// 다국어 테이블(pltf_lang_detail) 데이터 삭제플래그 처리
					LangDetailEntity ldEntity = new LangDetailEntity();
					ldEntity.setLdSeq(mlrEntityDel.getLdSeq());
					merchandizeLangRlMapper.deleteLang(ldEntity);
				}
			}
			return result;
		}
		return 0;
	}


	// 다국어 정보 신규 삽입(신규 저장할 경우, 기존정보에서 다국어 추가로 인한 기존정보수정과 신규저장을 병행할 경우 2가지의 경우의 수가 있어 분리함)
	/**
	 *
	 * @param dto 신규 삽입 또는 수정 정보가 담긴 dto
	 * @param categorytEntity 카테고리 테이블에 저장을 마치고 해당 테이블의 저장된 데이터 식별자를 가지고 있는 상품Entity
	 * @param isUpdatePartFlag	신규 삽입 건일 경우 false, 수정 화면에서 조작한 수정 정보 처리 중 신규 다국어 정보를 삽입해야 할 경우에 호출되었을 경우 true
	 * @param lkCount	현재 지원하고 있는 다국어 중 몇번째 순서의 다국어인지 판별하는 변수(ex: 과거에 지원하는 다국어가 2개였을 경우 저장된 데이터를 수정할 때 현재 지원하는 다국어가 3개라면, 다국어 리스트 배열의 3번째에 해당하는 숫자)
	 */
	private void insertLangInfo(MerchandizeCategoryDto dto, MerchandizeCategoryEntity categoryEntity, boolean isUpdatePartFlag, int lkCount) {
		LangEntity langEntity = new LangEntity();
		langEntity.setCorp(dto.getCorp());
		langEntity.setFgDelete(PlatformCommonVo.NO);
		//langEntity.setType("TXT");
		LangType.valueOf("TEXT");
		

		langMapper.insertLang(langEntity);	// 다국어 마스터 저장

		int lSeq = langEntity.getLSeq();
		if (lSeq > 0)
		{
			LangDetailEntity ldEntity = null;

			for (int i = 0; i < dto.getCategoryLangKindSeqs().length; i++)
			{
				// 수정 화면 처리에서 호출되었을 경우, 해당 다국어를 insert하는 경우가 아니면 pass하도록 처리
				if (isUpdatePartFlag)
				{
					if (i < lkCount)
					{
						continue;
					}
				}

				ldEntity = new LangDetailEntity();
				ldEntity.setLSeq(lSeq);
				ldEntity.setFgDelete(PlatformCommonVo.NO);
				ldEntity.setLkSeq(dto.getCategoryLangKindSeqs()[i]);
				ldEntity.setText(dto.getCategoryNames()[i]);

				langMapper.insertLangDetail(ldEntity);		// 다국어 상세 저장

				MerchandizeLangRlEntity mlrEntity = null;
				int ldSeq = ldEntity.getLdSeq();
				if (ldSeq > 0)
				{
					mlrEntity = new MerchandizeLangRlEntity();
					mlrEntity.setLdSeq(ldSeq);
					mlrEntity.setTSeq(categoryEntity.getMcSeq());
					mlrEntity.setType(EuMerchandizeCommonVo.CATEGORY_NAME);

					merchandizeLangRlMapper.insertItemDescription(mlrEntity);	// 머천다이즈 다국어 관계 저장
				}
			}
		}
	}
}
