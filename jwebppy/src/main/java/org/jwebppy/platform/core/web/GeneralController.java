package org.jwebppy.platform.core.web;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class GeneralController
{
	@Autowired
	private Environment environment;

	protected final String DEFAULT_VIEW_URL = "DUMMY";
	protected final String EMPTY_RETURN_VALUE = "";

	protected Integer getUSeq()
	{
		return UserAuthenticationUtils.getUserDetails().getUSeq();
	}

	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}

	protected String getPlatformName()
	{
		return environment.getProperty("platform.service");
	}

	protected boolean isProduction()
	{
		if (CmStringUtils.equals(getPlatformName(), "PRD"))
		{
			return true;
		}

		return false;
	}
}