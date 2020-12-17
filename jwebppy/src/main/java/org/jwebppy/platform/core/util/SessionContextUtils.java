package org.jwebppy.platform.core.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class SessionContextUtils
{
	public static String getSessionId()
	{
		return RequestContextHolder.currentRequestAttributes().getSessionId();
	}

    /**
     * attribute 값을 가져 오기 위한 method
     *
     * @param String  attribute key name
     * @return Object attribute obj
     */
    public static Object getAttribute(String name)
    {
        return RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * attribute 설정 method
     *
     * @param String  attribute key name
     * @param Object  attribute obj
     * @return void
     */
    public static void setAttribute(String name, Object object)
    {
        RequestContextHolder.getRequestAttributes().setAttribute(name, object, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 설정한 attribute 삭제
     *
     * @param String  attribute key name
     * @return void
     */
    public static void removeAttribute(String name)
    {
        RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }
}
