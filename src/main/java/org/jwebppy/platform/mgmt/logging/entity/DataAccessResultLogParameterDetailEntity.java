package org.jwebppy.platform.mgmt.logging.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessResultLogParameterDetailEntity extends GeneralEntity
{
	private static final long serialVersionUID = 691225609844960807L;

	private Long drlpSeq;
	private int lineNo;
	private String name;
	private String value;
}
