package org.jwebppy.portal.iv.eu.parts.order.create.dto;

import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuOrderHistoryItemDto extends EuGeneralDto
{
	private Integer ohhSeq;
	private String materialNo;
	private String orderQty;
}
