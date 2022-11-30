package org.jwebppy.platform.mgmt.content.entity;

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
public class CItemUserRlEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = -8656985794666270613L;

	private Integer curSeq;
	private Integer cSeq;
	private Integer uSeq;
	private int sort;
	private String name;
}
