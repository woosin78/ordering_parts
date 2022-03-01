package org.jwebppy.portal.iv.board.dto;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpBoardDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;

	private String bSeq;
	private String name;
	private String description;
	private String fgUseReply = PlatformCommonVo.NO;
	private String fgUseComment = PlatformCommonVo.NO;
	private String fgSetPeriod = PlatformCommonVo.NO;
	private String ufSeq;
	private String fgDelete = PlatformCommonVo.NO;
}
