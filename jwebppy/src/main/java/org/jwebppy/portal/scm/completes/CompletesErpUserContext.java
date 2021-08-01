package org.jwebppy.portal.scm.completes;

import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CompletesErpUserContext extends ErpUserContext
{
	private static final long serialVersionUID = 7566708895627849149L;

	public String salesDistct;
}
