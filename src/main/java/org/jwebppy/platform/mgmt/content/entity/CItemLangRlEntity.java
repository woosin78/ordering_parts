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
public class CItemLangRlEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = -8895372975976962612L;

	private Integer clrSeq;
	private Integer cSeq;
	private Integer lSeq;
}
