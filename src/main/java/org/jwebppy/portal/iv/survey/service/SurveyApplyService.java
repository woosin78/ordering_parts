package org.jwebppy.portal.iv.survey.service;

import java.util.List;

import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.survey.dto.SurveyApplyDto;
import org.jwebppy.portal.iv.survey.entity.SurveyApplyEntity;
import org.jwebppy.portal.iv.survey.mapper.SurveyApplyMapper;
import org.jwebppy.portal.iv.survey.mapper.SurveyApplyObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyApplyService extends PlatformGeneralService
{
	@Autowired
	private SurveyApplyMapper surveyApplyMapper;
	
	public List<SurveyApplyDto> getSurveyVoteItems(int sSeq) 
	{
		return CmModelMapperUtils.mapToDto(SurveyApplyObjectMapper.INSTANCE, surveyApplyMapper.findSurveyVoteItems(sSeq));
	}
	
	public int save(SurveyApplyDto surveyApplys) 
	{
		int cnt = 0;
		for (SurveyApplyDto surveyApply : surveyApplys.getItems()) 
		{
			SurveyApplyEntity surveyApplyEntity = CmModelMapperUtils.mapToEntity(SurveyApplyObjectMapper.INSTANCE, surveyApply);
			surveyApplyEntity.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
			surveyApplyEntity.setRegName(UserAuthenticationUtils.getUserDetails().getName());
			
			surveyApplyMapper.insert(surveyApplyEntity);
			cnt++;
		}
		
		return cnt;
	}
}
