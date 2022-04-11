package org.jwebppy.portal.iv.survey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.survey.dto.SurveyDto;
import org.jwebppy.portal.iv.survey.dto.SurveySearchDto;
import org.jwebppy.portal.iv.survey.entity.SurveyEntity;

@NoLogging
@Mapper
public interface SurveyMapper {

	public SurveyEntity findSurvey(SurveyDto survey);
	public List<SurveyEntity> findSurveys(SurveySearchDto surveySearchDto);
	public int insert(SurveyEntity survey);
}
