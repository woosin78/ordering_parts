package org.jwebppy.portal.iv.survey.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.survey.entity.SurveyItemEntity;

@NoLogging
@Mapper
public interface SurveyItemMapper {

	public List<SurveyItemEntity> findSurveyItems(int sqSeq);	
	public int insert(SurveyItemEntity surveyItemEntity);	
	public int update(SurveyItemEntity surveyItemEntity);
	public int delete(SurveyItemEntity surveyItemEntity);
	public int deleteBySseq(SurveyItemEntity surveyItemEntity);
}
