package org.jwebppy.portal.iv.hq.parts.promotion.dto;

import org.jwebppy.portal.iv.hq.parts.common.dto.PartsGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PromotionItemDto extends PartsGeneralDto
{
	private static final long serialVersionUID = 3915182747692161098L;

	private Integer piSeq;
	private Integer pSeq;
	private String materialNo;
	private String materialText;
}
