package org.jwebppy.platform.core.exception.security;

import org.springframework.security.core.AuthenticationException;

public class NoAuthorityException extends AuthenticationException
{
	private static final long serialVersionUID = 1L;

	public NoAuthorityException(String msg)
	{
		super(msg);
	}
}
