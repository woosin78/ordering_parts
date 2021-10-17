package org.jwebppy.platform.core;

public class PlatformConfigVo
{
	public final static String CONTEXT_PATH = "/platform";
	public final static String INDEX_URL = CONTEXT_PATH;
	public final static String ERROR_PAGE_URL = "/error";

	//Authentication
	public final static String FORM_AUTHENTICATION_PATH = CONTEXT_PATH + "/common/authentication";

	public final static String FORM_LOGIN_USERNAME = "username";
	public final static String FORM_LOGIN_PASSWORD = "password";
	public final static String FORM_LOGIN_PAGE_PATH = "/login";
	public final static String FORM_LOGIN_PROCESSING_PATH = "/login/check";
	public final static String FORM_LOGIN_PAGE_URL = FORM_AUTHENTICATION_PATH + FORM_LOGIN_PAGE_PATH;
	public final static String FORM_LOGIN_PROCESSING_URL = FORM_AUTHENTICATION_PATH + FORM_LOGIN_PROCESSING_PATH;
	public final static String FORM_LOGIN_FAIL_TYPE = "Login-Fail-Type";
	public final static int FORM_LOGIN_PASSWORD_FAIL_LIMIT_COUNT = 5;

	public final static String FORM_LOGOUT_PROCESSING_URL = FORM_AUTHENTICATION_PATH + "/logout";
	public final static String FORM_LOGOUT_SUCCESS_URL = INDEX_URL;

	public final static String FORM_PASSWORD_CHANGE_PAGE_PATH = "/change_password";
	public final static String FORM_PASSWORD_CHANGE_PROCESSING_PATH = "/change_password/save";
	public final static String FORM_PASSWORD_CHANGE_PAGE_URL = FORM_AUTHENTICATION_PATH + FORM_PASSWORD_CHANGE_PAGE_PATH;
	public final static String FORM_PASSWORD_CHANGE_PROCESSING_URL = FORM_AUTHENTICATION_PATH + FORM_PASSWORD_CHANGE_PROCESSING_PATH;

	//Authorization
	public final static String ROLE_PLTF_ADMIN = "PLTF_ADMIN";

	//Key
	public final static String REQUEST_MDC_UUID_TOKEN_KEY = "RUID";

	//DB
	public final static String MariaDB = "mariadb";
	public final static String Oracle = "oracle";
}
