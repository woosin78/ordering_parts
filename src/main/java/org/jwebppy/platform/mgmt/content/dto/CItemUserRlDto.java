package org.jwebppy.platform.mgmt.content.dto;

import java.util.List;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class CItemUserRlDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 7178684901907973459L;

	private Integer curSeq;
	private Integer cSeq;
	private Integer uSeq;
	@Builder.Default
	private int sort = 100;
	private List<Integer> cSeqs;
	private String name;
}
