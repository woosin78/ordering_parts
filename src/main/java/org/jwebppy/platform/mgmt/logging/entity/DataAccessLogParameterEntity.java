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
public class DataAccessLogParameterEntity extends GeneralEntity
{
	private static final long serialVersionUID = 298110073047414051L;
	private Long dlpSeq;
	private String dlSeq;
	private ParameterType type;//(S)Scalar, (R)Structure, (T)Table
	private String name;
	private List<DataAccessLogParameterDetailEntity> dataAccessLogParameterDetails;
}
