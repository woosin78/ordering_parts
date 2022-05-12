package org.jwebppy.portal.iv.eu.parts.order.create.entity;

import java.util.List;

import org.apache.poi.hpsf.Decimal;
import org.jwebppy.portal.iv.eu.common.entity.EuGeneralEntity;
import org.jwebppy.portal.iv.eu.parts.order.create.dto.EuOrderItemDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EuOrderItemEntity extends EuGeneralEntity
{
	private static final long serialVersionUID = -2495941487521285148L;
	private Integer mmSeq;
	private String lineNo;
	private String materialNo;
	private String materialName;
	private String startNo;
	private String lastNo;
	private String substituteNo;
	private String plant;
	private int orderQty;
	private int stockQty;
	private int unrestrictedQty;
	private int availableQty;
	private String description;
	private int minOrderQty;
	private String salesUnit;
	private String baseUnit;
	private Decimal netPrice;
	private Decimal netValue;
	private String error;
	private List<EuOrderItemDto> substitutes;
	private long tempSeq;

	public EuOrderItemEntity() {}
}
