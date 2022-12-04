package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class CredentialsPolicySearchDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = -8343332709660767762L;

	private Integer cpSeq;
	private CredentialsPolicyType type;
	private String name;
	private String fgUse;
	private String fgDefault;
	private String value;//username or password
	private String query;
	private Integer useq;
}
