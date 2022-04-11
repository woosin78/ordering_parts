package org.jwebppy.platform.mgmt.user.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CredentialsSecurityEntity extends GeneralEntity implements IPagination
{
	private static final long serialVersionUID = 7035335355415328343L;
}
