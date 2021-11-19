package org.jwebppy.portal.common;

import org.jwebppy.platform.core.PlatformConfigVo;

public class PortalConfigVo extends PlatformConfigVo
{
	public final static String CONTEXT_PATH = "/portal";
	public final static String INDEX_URL = CONTEXT_PATH;

	public final static String FORM_AUTHENTICATION_PATH = CONTEXT_PATH + "/common/authentication";
	public final static String FORM_LOGIN_PAGE_URL = FORM_AUTHENTICATION_PATH + FORM_LOGIN_PAGE_PATH; //폼 로그인 - 로그인 페이지 url
}
