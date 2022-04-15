package org.jwebppy.portal.iv.survey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.survey.entity.SurveyTargetEntity;

@NoLogging
@Mapper
public interface SurveyTargetMapper {

	public int insert(SurveyTargetEntity surveyTarget);
	public List<SurveyTargetEntity> findSurveyTargets(int sSeq);
	public int findSurveyTargetCount(int sSeq);
	public int delete(SurveyTargetEntity surveyTarget);
}
