package org.jwebppy.platform.mgmt.logging.entity;

import java.util.List;

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
public class DataAccessResultLogEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 5757265630316023771L;

	private Long drlSeq;
	private String dlSeq;
	private List<DataAccessResultLogParameterEntity> dataAccessResultLogParameters;
}
