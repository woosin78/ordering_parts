package org.jwebppy.platform.core.util;

import java.util.Locale;

import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthenticationUtils
{
	private static final String ROLE_PREFIX = "ROLE_";

	public static Authentication getAuthentication()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static PlatformUserDetails getUserDetails()
	{
		Authentication authentication = getAuthentication();

		if (authentication != null)
		{
			try
			{
				return (PlatformUserDetails)authentication.getDetails();
			}
			catch (ClassCastException e)
			{
				return new PlatformUserDetails();
			}
		}

		return new PlatformUserDetails();
	}

	public static Integer getUSeq()
	{
		PlatformUserDetails platformUserDetails = getUserDetails();

		if (platformUserDetails != null)
		{
			return platformUserDetails.getUSeq();
		}

		return null;
	}

	public static String getUsername()
	{
		try
		{
			return getAuthentication().getName();
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}

	public static String getLanguage()
	{
		return CmStringUtils.defaultString(UserAuthenticationUtils.getUserDetails().getLanguage(), Locale.ENGLISH).toUpperCase();
	}

	public static boolean isAuthenticated()
	{
		if (getUSeq() == null)
		{
			return false;
		}

		return true;
	}

	public static boolean isAccountNonExpired()
	{
		PlatformUserDetails platformUserDetails = getUserDetails();

		if (platformUserDetails != null)
		{
			return platformUserDetails.isAccountNonExpired();
		}

		return false;
	}

	public static boolean isAccountNonLocked()
	{
		PlatformUserDetails platformUserDetails = getUserDetails();

		if (platformUserDetails != null)
		{
			return platformUserDetails.isAccountNonLocked();
		}

		return false;
	}

	public static boolean isCredentialsNonExpired()
	{
		PlatformUserDetails platformUserDetails = getUserDetails();

		if (platformUserDetails != null)
		{
			return platformUserDetails.isCredentialsNonExpired();
		}

		return false;
	}

	public static boolean isEnabled()
	{
		PlatformUserDetails platformUserDetails = getUserDetails();

		if (platformUserDetails != null)
		{
			return platformUserDetails.isEnabled();
		}

		return false;
	}

	public static boolean hasRole(String[] roles)
	{
		if (roles == null || roles.length == 0)
		{
			return false;
		}

		for (String role : roles)
		{
			if (hasRole(role))
			{
				return true;
			}
		}

		return false;
	}

	public static boolean hasRole(String role)
	{
		Authentication authentication = getAuthentication();

		if (authentication == null)
		{
			return false;
		}

		for (GrantedAuthority auth : authentication.getAuthorities())
		{
			if (CmStringUtils.equals(ROLE_PREFIX + role, auth.getAuthority()))
			{
				return true;
			}
		}

		return false;
	}
}
