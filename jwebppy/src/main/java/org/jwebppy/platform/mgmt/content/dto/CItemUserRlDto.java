package org.jwebppy.platform.mgmt.content.dto;

import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemUserRlDto extends GeneralDto
{
	private static final long serialVersionUID = 7178684901907973459L;

	private Integer curSeq;
	private Integer cSeq;
	private Integer uSeq;
	private int sort = 100;
	private String fgDelete = PlatformCommonVo.NO;
	private List<Integer> cSeqs;
	private String name;
}
