package org.jwebppy.platform.mgmt.i18n.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangDetailEntity extends GeneralEntity
{
	private static final long serialVersionUID = 3608980239034591531L;

	private Integer ldSeq;
	private Integer lSeq;
	private Integer lkSeq;
	private String text;
	private LangKindEntity langKind;
}
