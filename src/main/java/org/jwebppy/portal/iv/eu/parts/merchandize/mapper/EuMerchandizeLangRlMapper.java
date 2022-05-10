package org.jwebppy.portal.iv.eu.parts.merchandize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.mgmt.i18n.entity.LangDetailEntity;
import org.jwebppy.portal.iv.eu.parts.merchandize.dto.MerchandizeLangRlDto;
import org.jwebppy.portal.iv.eu.parts.merchandize.entity.MerchandizeLangRlEntity;

@Mapper
public interface EuMerchandizeLangRlMapper
{
	public MerchandizeLangRlEntity findItemDescription(MerchandizeLangRlDto langDto);
	public List<MerchandizeLangRlEntity> findItemDescriptions(MerchandizeLangRlDto langDto);
	public int insertItemDescription(MerchandizeLangRlEntity merchandizeLangRlEntity);
	public int updateItemDescription(MerchandizeLangRlEntity merchandizeLangRlEntity);
	
	public List<MerchandizeLangRlEntity> findLangDetailSeqsForDelete(MerchandizeLangRlEntity merchandizeLangRlEntity);
	
	public List<MerchandizeLangRlEntity> findSupportLangsModify(MerchandizeLangRlEntity merchandizeLangRlEntity);	// 카테고리 수정 시 카테고리명 관련 정보 취득
	public int deleteLangDetail(MerchandizeLangRlEntity merchandizeLangRlEntity);	// 다국어 상세 테이블 데이터 삭제플래그 세우기
	public int deleteLang(LangDetailEntity langDetailEntity);	// 다국어 테이블 데이터 삭제플래그 세우기
	
}
