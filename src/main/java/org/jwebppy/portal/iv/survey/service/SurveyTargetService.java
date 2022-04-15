package org.jwebppy.portal.iv.survey.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.survey.dto.SurveyDto;
import org.jwebppy.portal.iv.survey.dto.SurveyTargetDto;
import org.jwebppy.portal.iv.survey.mapper.SurveyTargetMapper;
import org.jwebppy.portal.iv.survey.mapper.SurveyTargetObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyTargetService extends PlatformGeneralService
{
	@Autowired
	private SurveyTargetMapper surveyTargetMapper;
	
	public int save(SurveyTargetDto surveyTarget) 
	{
		return surveyTargetMapper.insert(CmModelMapperUtils.mapToEntity(SurveyTargetObjectMapper.INSTANCE, surveyTarget));
	}
	
	public int save(List<SurveyTargetDto> surveyTargets) throws IOException 
	{
		for (SurveyTargetDto surveyTarget : ListUtils.emptyIfNull(surveyTargets))
		{
			save(surveyTarget);
		}
		
		return 1;
	}
	
	public int save(SurveyDto survey) throws IOException 
	{
		if (surveyTargetMapper.findSurveyTargetCount(survey.getSSeq()) > 0) 
		{
			SurveyTargetDto surveyTarget = new SurveyTargetDto();
			surveyTarget.setSSeq(survey.getSSeq());
			delete(surveyTarget);
		}

		return save(survey.getSurveyTargets());
	}
	
	public List<SurveyTargetDto> getSurveyTargets(int sSeq) 
	{
		return CmModelMapperUtils.mapToDto(SurveyTargetObjectMapper.INSTANCE, surveyTargetMapper.findSurveyTargets(sSeq));
	}
	
	public int delete(SurveyTargetDto surveyTarget) {
		return surveyTargetMapper.delete(CmModelMapperUtils.mapToEntity(SurveyTargetObjectMapper.INSTANCE, surveyTarget));
	}
}
