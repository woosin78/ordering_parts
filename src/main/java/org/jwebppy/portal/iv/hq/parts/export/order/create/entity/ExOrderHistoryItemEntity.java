package org.jwebppy.portal.iv.hq.parts.export.order.create.entity;

import org.jwebppy.portal.iv.hq.parts.export.common.entity.PartsExportGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ExOrderHistoryItemEntity extends PartsExportGeneralEntity
{
	private static final long serialVersionUID = 6994471917634680143L;

	private Integer ohhSeq;
	private String materialNo;
	private String orderQty;
}
