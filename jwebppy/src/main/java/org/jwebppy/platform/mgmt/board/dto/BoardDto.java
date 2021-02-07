package org.jwebppy.platform.mgmt.board.dto;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardDto extends GeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;

	private Integer bSeq;
	private String bId;
	private String description;
	private String fgUseReply = PlatformCommonVo.NO;
	private String fgUseComment = PlatformCommonVo.NO;
	private String fgSetPeriod = PlatformCommonVo.NO;
	private Integer ufSeq;
	private String fgDelete = PlatformCommonVo.NO;
}
