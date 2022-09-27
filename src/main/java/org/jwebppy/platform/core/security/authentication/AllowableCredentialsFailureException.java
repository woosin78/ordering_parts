package org.jwebppy.platform.core.security.authentication;

import org.springframework.security.authentication.AccountStatusException;

public class AllowableCredentialsFailureException extends AccountStatusException
{
	private static final long serialVersionUID = -2313488379190220307L;

	public AllowableCredentialsFailureException(String msg)
	{
		super(msg);
	}

	public AllowableCredentialsFailureException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
