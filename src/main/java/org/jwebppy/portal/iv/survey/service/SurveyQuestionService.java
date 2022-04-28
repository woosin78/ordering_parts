package org.jwebppy.portal.iv.survey.service;

import java.util.List;

import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.survey.dto.SurveyItemDto;
import org.jwebppy.portal.iv.survey.dto.SurveyQuestionDto;
import org.jwebppy.portal.iv.survey.entity.SurveyQuestionEntity;
import org.jwebppy.portal.iv.survey.mapper.SurveyQuestionMapper;
import org.jwebppy.portal.iv.survey.mapper.SurveyQuestionObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyQuestionService extends PlatformGeneralService 
{
	@Autowired
	private SurveyQuestionMapper surveyQuestionMapper;
	
	@Autowired
	private SurveyItemService surveyItemService;
	
	public SurveyQuestionDto getSurveyQuestion(int sqSeq) 
	{
		return CmModelMapperUtils.mapToDto(SurveyQuestionObjectMapper.INSTANCE, surveyQuestionMapper.findSurveyQuestion(sqSeq));
	}
	
	public List<SurveyQuestionDto> getSurveyQuestions(int sSeq) 
	{
		return CmModelMapperUtils.mapToDto(SurveyQuestionObjectMapper.INSTANCE, surveyQuestionMapper.findSurveyQuestions(sSeq));
	}
	
	public int save(SurveyQuestionDto surveyQuestion) {
		if (surveyQuestion.getSqSeq() > 0) {
			return modify(surveyQuestion);
		} else {
			return create(surveyQuestion);			
		}
	}
	
	public int create(SurveyQuestionDto surveyQuestion) {
		SurveyQuestionEntity surveyQuestionEntity = CmModelMapperUtils.mapToEntity(SurveyQuestionObjectMapper.INSTANCE, surveyQuestion);
		surveyQuestionMapper.insert(surveyQuestionEntity);
		
		return surveyQuestionEntity.getSqSeq();
	}

	public int modify(SurveyQuestionDto surveyQuestion) {
		SurveyQuestionEntity surveyQuestionEntity = CmModelMapperUtils.mapToEntity(SurveyQuestionObjectMapper.INSTANCE, surveyQuestion);
		surveyQuestionMapper.update(surveyQuestionEntity);
		
		return surveyQuestionEntity.getSqSeq();
	}

	public int delete(SurveyQuestionDto surveyQuestion) {
		
		List<SurveyItemDto> surveyItems = surveyItemService.getSurveyItems(surveyQuestion.getSqSeq());
		if (surveyItems != null) {
			for (SurveyItemDto surveyItem : surveyItems) {
				surveyItemService.delete(surveyItem);
			}
		} 
		
		SurveyQuestionEntity surveyQuestionEntity = CmModelMapperUtils.mapToEntity(SurveyQuestionObjectMapper.INSTANCE, surveyQuestion);
		surveyQuestionMapper.delete(surveyQuestionEntity);
		
		return surveyQuestionEntity.getSqSeq();
	}
	
	public int deleteBySseq(SurveyQuestionDto surveyQuestion) {
		SurveyQuestionEntity surveyQuestionEntity = CmModelMapperUtils.mapToEntity(SurveyQuestionObjectMapper.INSTANCE, surveyQuestion);
		surveyQuestionMapper.deleteBySseq(surveyQuestionEntity);
		
		return surveyQuestionEntity.getSqSeq();
	}
}
