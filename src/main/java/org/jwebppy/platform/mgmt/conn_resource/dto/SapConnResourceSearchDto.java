package org.jwebppy.platform.mgmt.conn_resource.dto;

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
public class SapConnResourceSearchDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = -4241642481783873351L;

	private Integer scrSeq;
	private SapConnType type;
	private String query;
}
