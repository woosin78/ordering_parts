package org.jwebppy.portal.iv.survey.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.survey.entity.SurveyTargetEntity;

@NoLogging
@Mapper
public interface SurveyTargetMapper {

	public int insert(SurveyTargetEntity surveyTarget);
}
