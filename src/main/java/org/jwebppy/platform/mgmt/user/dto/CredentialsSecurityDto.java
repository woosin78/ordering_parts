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
public class CredentialsSecurityDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = 1366263438863190637L;
}
