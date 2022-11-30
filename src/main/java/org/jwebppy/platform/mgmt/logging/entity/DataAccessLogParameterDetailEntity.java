package org.jwebppy.platform.mgmt.logging.entity;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class DataAccessLogParameterDetailEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 8145782812462445407L;

	private Long dlpSeq;
	private int lineNo;
	private String name;
	private String value;
}
