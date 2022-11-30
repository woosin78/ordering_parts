package org.jwebppy.platform.mgmt.i18n.entity;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

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
public class LangKindEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = -8435384353160558730L;

	private Integer lkSeq;
	private String basename;
	private String name;
	private String code;
	private int sort;
	private String fgDefault;
}
