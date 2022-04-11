package org.jwebppy.platform.mgmt.conn_resource.dto;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SapConnResourceSearchDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -4241642481783873351L;

	private Integer scrSeq;
	private SapConnType type;
	private String query;
}
