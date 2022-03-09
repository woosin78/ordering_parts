package org.jwebppy.portal.iv.board.dto;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EpBoardDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;

	private String bSeq;
	private String name;
	private String description;
	@Builder.Default
	private String fgUseReply = PlatformCommonVo.NO;
	@Builder.Default
	private String fgUseComment = PlatformCommonVo.NO;
	@Builder.Default
	private String fgSetPeriod = PlatformCommonVo.NO;
	private String ufSeq;
	@Builder.Default
	private String fgDelete = PlatformCommonVo.NO;
}
