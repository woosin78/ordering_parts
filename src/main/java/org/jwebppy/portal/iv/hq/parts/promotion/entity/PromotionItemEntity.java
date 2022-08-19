package org.jwebppy.portal.iv.hq.parts.promotion.entity;

import org.jwebppy.portal.iv.hq.parts.common.entity.PartsGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PromotionItemEntity extends PartsGeneralEntity
{
	private static final long serialVersionUID = 7501998799786585300L;
	
	private Integer piSeq;
	private Integer pSeq;
	private String materialNo;

}
