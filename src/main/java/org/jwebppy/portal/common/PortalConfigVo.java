package org.jwebppy.portal.common;

import org.jwebppy.platform.core.PlatformConfigVo;

public class PortalConfigVo extends PlatformConfigVo
{
	public final static String CONTEXT_PATH = "/portal";
	public final static String INDEX_URL = CONTEXT_PATH;

	/*
	 * 인증
	 */
	public final static String FORM_AUTHENTICATION_PATH = CONTEXT_PATH + "/common/authentication";

	public final static String FORM_LOGIN_PAGE_PATH = "/login";
	public final static String FORM_LOGIN_PROCESSING_PATH = "/login/check";
	public final static String FORM_LOGIN_PAGE_URL = FORM_AUTHENTICATION_PATH + FORM_LOGIN_PAGE_PATH; //폼 로그인 - 로그인 페이지 url
	public final static String FORM_LOGIN_PROCESSING_URL = FORM_AUTHENTICATION_PATH + FORM_LOGIN_PROCESSING_PATH; //폼 로그인 - 로그인 처리 url

	public final static String FORM_LOGOUT_PROCESSING_URL = FORM_AUTHENTICATION_PATH + "/logout"; //폼 로그인 - 로그아웃 url
	public final static String FORM_LOGOUT_SUCCESS_URL = INDEX_URL; //폼 로그인 - 인증 성공 시 default 이동 url

	public final static String FORM_PASSWORD_CHANGE_PAGE_URL = FORM_AUTHENTICATION_PATH + FORM_PASSWORD_CHANGE_PAGE_PATH; //비밀번호 변경 url
}
