package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

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
public class CredentialsSecurityEntity extends MgmtGeneralEntity implements IPagination
{
	private static final long serialVersionUID = 7035335355415328343L;
}
