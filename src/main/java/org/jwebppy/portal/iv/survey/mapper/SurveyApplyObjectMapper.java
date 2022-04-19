package org.jwebppy.portal.iv.survey.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.survey.dto.SurveyApplyDto;
import org.jwebppy.portal.iv.survey.entity.SurveyApplyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SurveyApplyObjectMapper extends GeneralObjectMapper<SurveyApplyDto, SurveyApplyEntity>
{
	public SurveyApplyObjectMapper INSTANCE = Mappers.getMapper(SurveyApplyObjectMapper.class);
}
