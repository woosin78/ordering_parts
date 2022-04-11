package org.jwebppy.portal.iv.survey.service;

import java.io.IOException;
import java.util.List;

import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.survey.dto.SurveyDto;
import org.jwebppy.portal.iv.survey.dto.SurveySearchDto;
import org.jwebppy.portal.iv.survey.dto.SurveyTargetDto;
import org.jwebppy.portal.iv.survey.entity.SurveyEntity;
import org.jwebppy.portal.iv.survey.mapper.SurveyMapper;
import org.jwebppy.portal.iv.survey.mapper.SurveyObjectMapper;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyService extends PlatformGeneralService
{
	@Autowired
	private SurveyMapper surveyMapper;
	
	@Autowired
	private SurveyTargetService surveyTargetService;
	
	@Autowired
	private EpUploadFileListService uploadFileListService;
	
	public SurveyDto getSurvey(int sSeq) 
	{
		SurveyDto survey = new SurveyDto();
		survey.setSSeq(sSeq);
		
		return getSurvey(survey);
	}
	
	public SurveyDto getSurvey(SurveyDto survey) 
	{
		return CmModelMapperUtils.mapToDto(SurveyObjectMapper.INSTANCE, surveyMapper.findSurvey(survey));
	}

	public List<SurveyDto> getSurveys(SurveySearchDto surveySearchDto) 
	{
		return CmModelMapperUtils.mapToDto(SurveyObjectMapper.INSTANCE, surveyMapper.findSurveys(surveySearchDto));
	}
	
	public int save(SurveyDto survey) throws IOException
	{
		if (survey.getSSeq() == 0) {
			return create(survey);
		} 
		else 
		{
			return modify(survey);
		}
	}
	
	public int create(SurveyDto survey) throws IOException
	{
		SurveyEntity surveyEntity = CmModelMapperUtils.mapToEntity(SurveyObjectMapper.INSTANCE, survey);
		surveyMapper.insert(surveyEntity);
		
		int sSeq = surveyEntity.getSSeq();
		for (SurveyTargetDto surveyTarget: survey.getSurveyTargets()) {
			surveyTarget.setSSeq(sSeq);
		}
		
		uploadFileListService.save(survey.getUploadFile(), Integer.toString(sSeq), survey.getFiles());
		surveyTargetService.save(survey.getSurveyTargets());
		
		return sSeq;
	}
	
	public int modify(SurveyDto survey) throws IOException
	{
		SurveyEntity surveyEntity = CmModelMapperUtils.mapToEntity(SurveyObjectMapper.INSTANCE, survey);
		
		
		return 0;
	}
}
