package org.jwebppy.platform.mgmt.logging.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessLogParameterDetailEntity extends GeneralEntity
{
	private static final long serialVersionUID = 8145782812462445407L;
	private Long dlpSeq;
	private int lineNo;
	private String name;
	private String value;
}
