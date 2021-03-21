package org.jwebppy.platform.mgmt.logging.entity;

import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessResultLogEntity extends GeneralEntity
{
	private static final long serialVersionUID = 5757265630316023771L;

	private Long drlSeq;
	private String dlSeq;
	private List<DataAccessResultLogParameterEntity> dataAccessResultLogParameters;
}
