package org.jwebppy.platform.mgmt.logging.entity;

import java.util.List;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;

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
public class DataAccessLogParameterEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 298110073047414051L;

	private Long dlpSeq;
	private String dlSeq;
	private ParameterType type;//(S)Scalar, (R)Structure, (T)Table
	private String name;
	private List<DataAccessLogParameterDetailEntity> dataAccessLogParameterDetails;
}
