package org.jwebppy.platform.mgmt.i18n.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangKindEntity extends GeneralEntity
{
	private static final long serialVersionUID = -8435384353160558730L;

	private Integer lkSeq;
	private String basename;
	private String name;
	private String code;
	private int sort;
	private String fgDefault;
}
