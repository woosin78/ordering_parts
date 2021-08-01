package org.jwebppy.portal.scm.parts;

import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PartsErpUserContext extends ErpUserContext
{
	private static final long serialVersionUID = -2865664308679960779L;

	private String custGrp5;
	private String custType;
}
