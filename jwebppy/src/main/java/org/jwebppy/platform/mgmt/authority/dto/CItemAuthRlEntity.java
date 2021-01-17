package org.jwebppy.platform.mgmt.authority.dto;

import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemAuthRlEntity extends GeneralEntity
{
	private static final long serialVersionUID = -7692406554907504005L;

	private Integer carSeq;
	private Integer pSeq;
	private Integer cSeq;
	private int sort;
	private String fgDelete;
	private List<Integer> cSeqs;
}
