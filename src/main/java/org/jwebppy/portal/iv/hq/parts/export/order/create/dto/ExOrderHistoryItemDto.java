package org.jwebppy.portal.iv.hq.parts.export.order.create.dto;

import org.jwebppy.portal.iv.hq.parts.export.common.dto.PartsExportGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExOrderHistoryItemDto extends PartsExportGeneralDto
{
	private static final long serialVersionUID = 3893094058317571949L;

	private Integer ohhSeq;
	private String materialNo;
	private String orderQty;
}
