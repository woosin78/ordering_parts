package org.jwebppy.portal.iv.survey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.survey.entity.SurveyApplyEntity;

@NoLogging
@Mapper
public interface SurveyApplyMapper {

	public List<SurveyApplyEntity> findSurveyVoteItems(int sSeq);
	public List<SurveyApplyEntity> findSurveyResult(int sSeq);
	public List<SurveyApplyEntity> findSurveyTextAnswers(int sSeq);
	public int insert(SurveyApplyEntity surveyVote);
}
