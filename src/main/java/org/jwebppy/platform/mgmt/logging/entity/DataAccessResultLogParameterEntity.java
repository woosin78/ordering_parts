package org.jwebppy.platform.mgmt.logging.entity;

import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessResultLogParameterEntity extends GeneralEntity
{
	private static final long serialVersionUID = 1416163331130996193L;

	private Long drlpSeq;
	private Long drlSeq;
	private ParameterType type;//(S)Scalar, (R)Structure, (T)Table
	private String name;
	private List<DataAccessResultLogParameterDetailEntity> dataAccessResultLogParameterDetails;
}
