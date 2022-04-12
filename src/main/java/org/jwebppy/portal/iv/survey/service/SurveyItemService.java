package org.jwebppy.portal.iv.survey.service;

import java.util.List;

import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.survey.dto.SurveyItemDto;
import org.jwebppy.portal.iv.survey.entity.SurveyItemEntity;
import org.jwebppy.portal.iv.survey.mapper.SurveyItemMapper;
import org.jwebppy.portal.iv.survey.mapper.SurveyItemObjectMapper;
import org.jwebppy.portal.iv.survey.mapper.SurveyQuestionObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyItemService extends PlatformGeneralService 
{
	@Autowired
	private SurveyItemMapper surveyItemMapper;
	
	public List<SurveyItemDto> getSurveyItems(int sqSeq) 
	{
		return CmModelMapperUtils.mapToDto(SurveyItemObjectMapper.INSTANCE, surveyItemMapper.findSurveyItems(sqSeq));
	}
	
	public int save(SurveyItemDto surveyItem) {
		return create(surveyItem);
	}
	
	public int create(SurveyItemDto surveyItem) {
		
		SurveyItemEntity surveyItemEntity = CmModelMapperUtils.mapToEntity(SurveyItemObjectMapper.INSTANCE, surveyItem);
		surveyItemMapper.insert(surveyItemEntity);
		
		return surveyItemEntity.getSiSeq();
	}
}
