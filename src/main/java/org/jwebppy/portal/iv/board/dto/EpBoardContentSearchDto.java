package org.jwebppy.portal.iv.board.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpBoardContentSearchDto extends IvGeneralDto
{
	private static final long serialVersionUID = -6795655439701455791L;

	private String corp;
	private String bcSeq;
	private String bSeq;
	private String title;
	private String writer;
	private String fromRegDate;
	private String toRegDate;
	private String fromView;
	private String toView;
	private String custCode;
}
