package org.jwebppy.portal.iv.hq.parts.domestic.order.create.dto;

import org.jwebppy.portal.iv.hq.parts.domestic.common.dto.PartsDomesticGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderHistoryItemDto extends PartsDomesticGeneralDto
{
	private static final long serialVersionUID = 3893094058317571949L;

	private Integer ohhSeq;
	private String materialNo;
	private String orderQty;
	private int sort;
}
