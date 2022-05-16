package org.jwebppy.portal.iv.uk.parts.domestic.order.create.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.portal.iv.uk.common.entity.UkGeneralEntity;
import org.jwebppy.portal.iv.uk.parts.domestic.order.create.dto.UkOrderHistoryItemDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UkOrderHistoryHeaderEntity extends UkGeneralEntity
{
	private static final long serialVersionUID = 1L;

	private Integer ohhSeq;
	private String docType;
	private String orderType;
	private String soldToNo;
	private String shipToNo;
	private Integer oaSeq;
	private String salesOrg;
	private String distChannel;
	private String division;
	private String shippingCondition;
	private String poNo;
	private String soNo;
	private String errorMsg;
	private LocalDateTime orderedDate;
	private Integer duplOhhSeq;
	private List<UkOrderHistoryItemDto> orderHistoryItems;
}
