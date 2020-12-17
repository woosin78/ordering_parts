package org.jwebppy.platform.core;

public class PlatformConfigVo
{
	public final static String INDEX_URL = "/";
	public final static String ERROR_PAGE_URL = "/error";

	//Authentication
	public final static String FORM_LOGON_USERNAME = "username";
	public final static String FORM_LOGON_PASSWORD = "password";
	public final static String FORM_LOGON_PAGE_URL = "/platform/security/authentication/login_form";
	public final static String FORM_LOGON_PROCESSING_URL = "/platform/security/authentication/check";
	public final static String FORM_LOGOUT_PROCESSING_URL = "/platform/security/authentication/logout";
	public final static String FORM_LOGOUT_SUCCESS_URL = "/";
	public final static String FORM_LOGON_USERNAME_RULE_PATTERN = "^((?=.*[a-zA-Z])|(?=.*\\d)).{6,12}$";
	public final static String FORM_LOGON_PASSWORD_RULE_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";
	public final static int FORM_LOGON_PASSWORD_FAIL_LIMIT_COUNT = 5;

	//Authorization
	public final static String ROLE_SUPER_ADMIN = "PLTF_SUPER_ADMIN";

	public final static String REQUEST_MDC_UUID_TOKEN_KEY = "RUID";
}
