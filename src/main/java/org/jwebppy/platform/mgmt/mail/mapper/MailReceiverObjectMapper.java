package org.jwebppy.platform.mgmt.mail.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.mail.dto.MailReceiverDto;
import org.jwebppy.platform.mgmt.mail.entity.MailReceiverEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MailReceiverObjectMapper extends GeneralObjectMapper<MailReceiverDto, MailReceiverEntity>
{
	public MailReceiverObjectMapper INSTANCE = Mappers.getMapper(MailReceiverObjectMapper.class);
}
