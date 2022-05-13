package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.dto;

import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
public class MerchandizeMainDto extends EuGeneralDto
{
	private Integer mmmSeq;
	private String corp;

	public MerchandizeMainDto() {}
}
