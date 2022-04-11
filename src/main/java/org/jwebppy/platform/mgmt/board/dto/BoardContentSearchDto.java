package org.jwebppy.platform.mgmt.board.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardContentSearchDto extends GeneralDto
{
	private static final long serialVersionUID = -6795655439701455791L;

	private String bId;
	private Integer bcSeq;
	private Integer bSeq;
	private Integer pSeq;
	private Integer ufSeq;
	private String title;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private String type = "TC";
	private String query;
	private LocalDateTime fromView;
	private LocalDateTime toView;
}
