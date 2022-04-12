package org.jwebppy.portal.iv.survey.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.survey.dto.SurveyQuestionDto;
import org.jwebppy.portal.iv.survey.entity.SurveyQuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SurveyQuestionObjectMapper extends GeneralObjectMapper<SurveyQuestionDto, SurveyQuestionEntity>
{
	public SurveyQuestionObjectMapper INSTANCE = Mappers.getMapper(SurveyQuestionObjectMapper.class);
}
