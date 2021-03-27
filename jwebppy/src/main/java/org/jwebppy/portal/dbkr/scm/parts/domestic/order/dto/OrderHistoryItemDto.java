package org.jwebppy.portal.dbkr.scm.parts.domestic.order.dto;

import org.jwebppy.portal.dbkr.scm.parts.PartsGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderHistoryItemDto extends PartsGeneralDto
{
	private static final long serialVersionUID = 3893094058317571949L;

	private Integer ohhSeq;
	private String materialNo;
	private String orderQty;
}