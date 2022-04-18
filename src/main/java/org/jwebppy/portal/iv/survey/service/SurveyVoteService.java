package org.jwebppy.portal.iv.survey.service;

import java.util.List;

import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.survey.dto.SurveyVoteDto;
import org.jwebppy.portal.iv.survey.mapper.SurveyVoteMapper;
import org.jwebppy.portal.iv.survey.mapper.SurveyVoteObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyVoteService extends PlatformGeneralService
{
	@Autowired
	private SurveyVoteMapper surveyVoteMapper;
	
	public List<SurveyVoteDto> getSurveyVoteItems(int sSeq) 
	{
		return CmModelMapperUtils.mapToDto(SurveyVoteObjectMapper.INSTANCE, surveyVoteMapper.findSurveyVoteItems(sSeq));
	}
}
