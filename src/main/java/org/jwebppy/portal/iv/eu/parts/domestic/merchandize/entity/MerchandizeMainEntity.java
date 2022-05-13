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
public class MerchandizeMainEntity extends EuGeneralEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2597034807191081870L;
	
	private Integer mmmSeq;
	private String corp;

	public MerchandizeMainEntity() {}
}
