package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.service;

import java.util.List;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto.MerchandizeLangRlDto;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity.MerchandizeLangRlEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.merchandize.mapper.EuMerchandizeLangRlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EuMerchandizeLangRlService
{
	@Autowired
	private EuMerchandizeLangRlMapper merchandizeLangRlMapper;
	

	// 카테고리 수정 시 다국어 정보 n건 취득
	public List<MerchandizeLangRlDto> getSupportLangsModify(MerchandizeLangRlEntity merchandizeLangRlEntity) 
	{		
		List<MerchandizeLangRlEntity> langEntities = merchandizeLangRlMapper.findSupportLangsModify(merchandizeLangRlEntity);		
		return CmModelMapperUtils.mapAll(langEntities, MerchandizeLangRlDto.class);		
	}	
}
