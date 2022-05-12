package org.jwebppy.portal.iv.eu.parts.merchandize.dto;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.portal.iv.eu.common.dto.EuGeneralDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
public class MerchandizeProductSearchDto extends EuGeneralDto implements IPagination
{	
	private Integer mpSeq;
	private String categoryCode;
	private String productCodeOrName;
	private String fgDisplay;

	private String productImagePath;	// 파일 경로

	public MerchandizeProductSearchDto() {}
}
