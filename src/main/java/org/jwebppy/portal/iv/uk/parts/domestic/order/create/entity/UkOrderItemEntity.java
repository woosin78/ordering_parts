package org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity;

import java.util.List;

import org.apache.poi.hpsf.Decimal;
import org.jwebppy.portal.iv.uk.common.entity.UkGeneralEntity;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderItemDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UkOrderItemEntity extends UkGeneralEntity
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
	private List<UkOrderItemDto> substitutes;
	private long tempSeq;

	public UkOrderItemEntity() {}
}
