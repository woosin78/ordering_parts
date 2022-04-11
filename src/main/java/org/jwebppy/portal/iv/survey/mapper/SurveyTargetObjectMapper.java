package org.jwebppy.portal.iv.survey.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.survey.dto.SurveyTargetDto;
import org.jwebppy.portal.iv.survey.entity.SurveyTargetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SurveyTargetObjectMapper extends GeneralObjectMapper<SurveyTargetDto, SurveyTargetEntity>
{
	public SurveyTargetObjectMapper INSTANCE = Mappers.getMapper(SurveyTargetObjectMapper.class);
}
