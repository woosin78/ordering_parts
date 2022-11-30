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
public class DataAccessResultLogParameterDetailEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 691225609844960807L;

	private Long drlpSeq;
	private int lineNo;
	private String name;
	private String value;
}
