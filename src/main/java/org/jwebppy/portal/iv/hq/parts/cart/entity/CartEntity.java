package org.jwebppy.portal.iv.hq.parts.cart.entity;

import java.util.List;

import org.jwebppy.portal.iv.hq.parts.common.entity.PartsGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CartEntity extends PartsGeneralEntity
{
	private static final long serialVersionUID = 664717208735966711L;

	private Integer ciSeq;
	private Integer uSeq;
	private String materialNo;
	private String orderQty;
	private Integer ohhSeq;

	private List<String> materialNos;
}
