package org.jwebppy.platform.mgmt.user.dto;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CredentialsPolicySearchDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -8343332709660767762L;

	private Integer cpSeq;
	private CredentialsPolicyType type;
	private String name;
	private String fgUse;
	private String fgDefault;
	private String value;
	private String query;
}
