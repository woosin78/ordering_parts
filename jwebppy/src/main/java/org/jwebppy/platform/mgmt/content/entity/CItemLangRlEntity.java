package org.jwebppy.platform.mgmt.content.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemLangRlEntity extends GeneralEntity
{
	private static final long serialVersionUID = -8895372975976962612L;
	private Integer clrSeq;
	private Integer cSeq;
	private Integer lSeq;
}
