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
public class LangDetailEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 3608980239034591531L;

	private Integer ldSeq;
	private Integer lseq;
	private Integer lkSeq;
	private String text;
	private LangKindEntity langKind;
}
