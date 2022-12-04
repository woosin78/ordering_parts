package org.jwebppy.platform.mgmt.authority.entity;

import java.util.List;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

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
public class CItemAuthRlEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = -7692406554907504005L;

	private Integer carSeq;
	private Integer pseq;
	private Integer cseq;
	private int sort;
	private List<Integer> cseqs;
}
