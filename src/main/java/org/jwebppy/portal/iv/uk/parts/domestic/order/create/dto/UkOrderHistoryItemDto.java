package org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto;

import org.jwebppy.portal.iv.uk.common.dto.UkGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UkOrderHistoryItemDto extends UkGeneralDto
{
	private Integer ohhSeq;
	private String materialNo;
	private String orderQty;
}
