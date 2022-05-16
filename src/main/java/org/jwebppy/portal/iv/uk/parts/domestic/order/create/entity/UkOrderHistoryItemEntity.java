package org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity;


import org.jwebppy.portal.iv.uk.common.entity.UkGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UkOrderHistoryItemEntity extends UkGeneralEntity
{
	private static final long serialVersionUID = 1L;

	private Integer ohhSeq;
	private String materialNo;
	private String orderQty;
}
