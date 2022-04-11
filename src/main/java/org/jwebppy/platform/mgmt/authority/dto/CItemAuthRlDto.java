package org.jwebppy.platform.mgmt.authority.dto;

import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CItemAuthRlDto extends GeneralDto
{
	private static final long serialVersionUID = 2791017870334138849L;

	private Integer carSeq;
	private Integer pSeq;
	private Integer cSeq;
	private int sort = -1;
	private List<Integer> cSeqs;

	public int getSort()
	{
		if (sort < 0)
		{
			return 100;
		}

		return sort;
	}
}
