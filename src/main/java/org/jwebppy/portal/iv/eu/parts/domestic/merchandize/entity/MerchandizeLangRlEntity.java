package org.jwebppy.portal.iv.eu.parts.domestic.merchandize.entity;

import org.jwebppy.portal.iv.eu.common.entity.EuGeneralEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class MerchandizeLangRlEntity extends EuGeneralEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4840033982484482174L;
	
	private Integer mlrSeq;
	private Integer tSeq;
	private Integer ldSeq;
	private String type;
	
	private Integer lSeq;	
	private Integer lkSeq;
	private String text;
	private String code;
	private String name;
	
	public MerchandizeLangRlEntity() {}
}
