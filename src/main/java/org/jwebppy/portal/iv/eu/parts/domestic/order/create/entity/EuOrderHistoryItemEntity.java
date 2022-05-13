package org.jwebppy.portal.iv.eu.parts.domestic.order.create.entity;

import org.jwebppy.portal.iv.eu.common.entity.EuGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuOrderHistoryItemEntity extends EuGeneralEntity
{
	private static final long serialVersionUID = 1L;

	private Integer ohhSeq;
	private String materialNo;
	private String orderQty;
}
