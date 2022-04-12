package org.jwebppy.portal.iv.survey.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.survey.dto.SurveyItemDto;
import org.jwebppy.portal.iv.survey.entity.SurveyItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SurveyItemObjectMapper extends GeneralObjectMapper<SurveyItemDto, SurveyItemEntity>
{
	public SurveyItemObjectMapper INSTANCE = Mappers.getMapper(SurveyItemObjectMapper.class);
}
