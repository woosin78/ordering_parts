package org.jwebppy.portal.iv.hq.parts.cart.dto;

import java.util.List;

import org.jwebppy.portal.iv.hq.parts.common.dto.PartsGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CartDto extends PartsGeneralDto
{
	private static final long serialVersionUID = 4966926989263909123L;

	private Integer ciSeq;
	private Integer uSeq;
	private String materialNo;
	private String description;
	private String lotQty;
	private String orderQty;
	private Integer ohhSeq;

	private List<Integer> ciSeqs;
	private List<String> materialNos;

}
