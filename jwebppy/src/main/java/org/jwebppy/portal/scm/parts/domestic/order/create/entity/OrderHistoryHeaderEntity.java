package org.jwebppy.portal.scm.parts.domestic.order.create.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.portal.scm.parts.PartsGeneralEntity;
import org.jwebppy.portal.scm.parts.domestic.order.create.dto.OrderHistoryItemDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderHistoryHeaderEntity extends PartsGeneralEntity
{
	private static final long serialVersionUID = -6805752046058134209L;

	private Integer ohhSeq;
	private String corp;
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
	private List<OrderHistoryItemDto> orderHistoryItems;
}