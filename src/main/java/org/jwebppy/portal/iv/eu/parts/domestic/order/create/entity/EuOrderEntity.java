package org.jwebppy.portal.iv.eu.parts.domestic.order.create.entity;

import java.util.List;

import org.jwebppy.portal.iv.eu.common.entity.EuGeneralEntity;
import org.jwebppy.portal.iv.eu.parts.domestic.order.create.dto.EuOrderItemDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuOrderEntity extends EuGeneralEntity
{
	private static final long serialVersionUID = -7362081609515377532L;
	private String erpOrderNo;
	private String orderNo;
	private String docType;
	private String customerNo;
	private String salesOrg;
	private String distChannel;
	private String division;
	private String plant;
	private String sellToNo;
	private String deliveredToNo;
	private String shippingCondition;
	private String memo;
	private List<EuOrderItemDto> orderItems;
	private String tempSeq;

	public EuOrderEntity() {}
}
