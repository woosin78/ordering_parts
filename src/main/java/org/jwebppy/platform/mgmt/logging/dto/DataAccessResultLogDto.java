package org.jwebppy.platform.mgmt.logging.dto;

import java.util.List;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

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
public class DataAccessResultLogDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = 7774886530956940272L;

	private Long drlSeq;
	private String dlSeq;
	private List<DataAccessResultLogParameterDto> dataAccessResultLogParameters;
}
