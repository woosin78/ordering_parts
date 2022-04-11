package org.jwebppy.platform.mgmt.logging.dto;

import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessResultLogDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = 7774886530956940272L;

	private Long drlSeq;
	private String dlSeq;
	private List<DataAccessResultLogParameterDto> dataAccessResultLogParameters;
}
