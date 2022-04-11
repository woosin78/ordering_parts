package org.jwebppy.portal.iv.survey.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.survey.dto.SurveyDto;
import org.jwebppy.portal.iv.survey.entity.SurveyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SurveyObjectMapper extends GeneralObjectMapper<SurveyDto, SurveyEntity>
{
	public SurveyObjectMapper INSTANCE = Mappers.getMapper(SurveyObjectMapper.class);
}
