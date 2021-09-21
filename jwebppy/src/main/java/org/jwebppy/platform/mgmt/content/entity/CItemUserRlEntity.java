package org.jwebppy.platform.mgmt.content.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemUserRlEntity extends GeneralEntity
{
	private static final long serialVersionUID = -8656985794666270613L;
	private Integer curSeq;
	private Integer cSeq;
	private Integer uSeq;
	private int sort;
	private String name;
}
