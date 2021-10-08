package org.jwebppy.platform.mgmt.user.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.entity.CredentialsPolicyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CredentialsPolicyObjectMapper extends GeneralObjectMapper<CredentialsPolicyDto, CredentialsPolicyEntity>
{
	public CredentialsPolicyObjectMapper INSTANCE = Mappers.getMapper(CredentialsPolicyObjectMapper.class);
}
