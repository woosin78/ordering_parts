package org.jwebppy.platform.mgmt.board.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.springframework.format.annotation.DateTimeFormat;

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
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime fromDate;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime toDate;
	private String type = "TC";
	private String query;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime fromView;
	@DateTimeFormat(pattern = PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT)
	private LocalDateTime toView;
}
