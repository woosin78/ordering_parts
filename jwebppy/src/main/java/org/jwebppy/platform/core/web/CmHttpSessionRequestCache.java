package org.jwebppy.platform.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

public class CmHttpSessionRequestCache extends HttpSessionRequestCache
{
	@Override
	public void saveRequest(HttpServletRequest request, HttpServletResponse response)
	{
		if (CmStringUtils.notEquals(request.getHeader("x-requested-with"), "XMLHttpRequest"))
		{
			super.saveRequest(request, response);
		}
	}
}
