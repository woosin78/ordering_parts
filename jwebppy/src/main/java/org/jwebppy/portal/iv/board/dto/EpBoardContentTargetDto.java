package org.jwebppy.portal.iv.board.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpBoardContentTargetDto extends IvGeneralDto
{
	private static final long serialVersionUID = -5101844495092026587L;

	private String bcSeq;
	private String code;
	private String description;
	private String type;
}
