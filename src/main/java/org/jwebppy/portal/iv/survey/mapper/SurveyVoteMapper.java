package org.jwebppy.portal.iv.survey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.survey.entity.SurveyVoteEntity;

@NoLogging
@Mapper
public interface SurveyVoteMapper {

	public List<SurveyVoteEntity> findSurveyVoteItems(int sSeq);
}
