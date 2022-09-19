package org.jwebppy.portal.iv.hq.parts.promotion.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.util.CmMyBatisQueryUtils;
import org.jwebppy.portal.iv.hq.parts.common.dto.PartsGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PromotionSearchDto extends PartsGeneralDto
{
	private static final long serialVersionUID = 7436604637369814540L;

	private Integer pSeq;
	private String title;
	private String fromView;
	private String toView;
	private String writer;
	private String fromRegDate;
	private String toRegDate;
	private String custCode;
	private String state;
	private String target;

	public LocalDateTime getFromRegDateForDb()
	{
		return CmMyBatisQueryUtils.toDate(fromRegDate, "FROM");
	}

	public LocalDateTime getToRegDateForDb()
	{
		return CmMyBatisQueryUtils.toDate(toRegDate, "TO");
	}

}
