package org.jwebppy.portal.iv.survey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.survey.entity.SurveyQuestionEntity;

@NoLogging
@Mapper
public interface SurveyQuestionMapper {

	public SurveyQuestionEntity findSurveyQuestion(int sqSeq);

	public List<SurveyQuestionEntity> findSurveyQuestions(int sSeq);
	
	public int insert(SurveyQuestionEntity surveyQuestion);
	
	public int update(SurveyQuestionEntity surveyQuestion);
	
	public int delete(SurveyQuestionEntity surveyQuestion);
}
