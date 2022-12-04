package org.jwebppy.platform.mgmt.authority.dto;

import java.util.List;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

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
public class CItemAuthRlDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 2791017870334138849L;

	private Integer carSeq;
	private Integer pseq;
	private Integer cseq;
	@Builder.Default
	private int sort = -1;
	private List<Integer> cseqs;

	public int getSort()
	{
		if (sort < 0)
		{
			return 100;
		}

		return sort;
	}
}
