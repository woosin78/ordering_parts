package org.jwebppy.platform.core.web;

import java.util.Iterator;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

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

    protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
    {
        Iterator<?> it = webRequest.getParameterNames();
        String name = null;
        String[] value = null;
        int length = 0;

        while (it.hasNext())
        {
            name = (String)it.next();
            value = webRequest.getParameterValues(name);

            if (value != null)
            {
                length = value.length;

                if (length == 1)
                {
                    model.addAttribute(name, value[0]);
                }
                else
                {
                    model.addAttribute(name, value);
                }
            }
        }
    }
}