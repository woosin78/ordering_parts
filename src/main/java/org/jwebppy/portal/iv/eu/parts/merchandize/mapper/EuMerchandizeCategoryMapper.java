package org.jwebppy.portal.iv.eu.parts.merchandize.mapper;

import java.util.List;

import org.jwebppy.platform.mgmt.i18n.entity.LangKindEntity;
import org.jwebppy.portal.iv.eu.parts.merchandize.dto.MerchandizeCategoryDto;
import org.jwebppy.portal.iv.eu.parts.merchandize.entity.MerchandizeCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EuMerchandizeCategoryMapper
{
	public MerchandizeCategoryEntity findCategoryItem(MerchandizeCategoryDto categoryDto);
	public List<MerchandizeCategoryEntity> findCategoryItems(MerchandizeCategoryDto categoryDto);
	public MerchandizeCategoryEntity findCategoryDuplicateCode(MerchandizeCategoryDto categoryDto);
	public MerchandizeCategoryEntity findCategoryNewSortNumber(MerchandizeCategoryDto categoryDto);
	public int insertCategoryItem(MerchandizeCategoryEntity category);
	public int updateCategoryItem(MerchandizeCategoryEntity category);
//	public int deleteCategoryItem(MerchandizeCategoryEntity category);	
	
	public MerchandizeCategoryEntity findCategoryItemsMainCount(MerchandizeCategoryDto categoryDto);
	
	public List<LangKindEntity> findSupportLangs(LangKindEntity langKindEntity);	// 지원하는 언어 리스트 취득
}
