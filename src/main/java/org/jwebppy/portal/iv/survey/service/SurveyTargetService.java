package org.jwebppy.portal.iv.survey.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
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
}
