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
public class DataAccessResultLogParameterEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 1416163331130996193L;

	private Long drlpSeq;
	private Long drlSeq;
	private ParameterType type;//(S)Scalar, (R)Structure, (T)Table
	private String name;
	private List<DataAccessResultLogParameterDetailEntity> dataAccessResultLogParameterDetails;
}
