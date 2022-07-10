package org.jwebppy.platform.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.dto.ServletRequestInfoDto;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.ThreadLocalContextUtils;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class PlatformRequestFilter implements Filter
{
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		isAllowableMethod(request, response);

		ServletRequestInfoDto servletRequestInfo = ServletRequestInfoDto.builder()
				.requestId(UidGenerateUtils.generate())
				.requestUri(((HttpServletRequest)request).getRequestURI())
				.referer(((HttpServletRequest)request).getHeader("referer"))
				.build();

		ThreadLocalContextUtils.servletRequestInfo.set(servletRequestInfo);

		chain.doFilter(request, response);
	}

	protected boolean isAllowableMethod(ServletRequest request, ServletResponse response) throws IOException
	{
		HttpServletRequest httpRequest = (HttpServletRequest)request;

		String method = CmStringUtils.trimToEmpty(httpRequest.getMethod());

		if (HttpMethod.GET.matches(method) || HttpMethod.POST.matches(method))
		{
			return true;
		}
		else
		{
			((HttpServletResponse)response).sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
			return false;
		}
	}
}
