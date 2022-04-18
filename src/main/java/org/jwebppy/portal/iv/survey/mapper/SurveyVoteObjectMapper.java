package org.jwebppy.portal.iv.survey.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.survey.dto.SurveyVoteDto;
import org.jwebppy.portal.iv.survey.entity.SurveyVoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SurveyVoteObjectMapper extends GeneralObjectMapper<SurveyVoteDto, SurveyVoteEntity>
{
	public SurveyVoteObjectMapper INSTANCE = Mappers.getMapper(SurveyVoteObjectMapper.class);
}
