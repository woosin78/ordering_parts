package org.jwebppy.platform.core;

public class PlatformConfigVo
{
	public final static String CONTEXT_PATH = "/platform";
	public final static String INDEX_URL = CONTEXT_PATH;
	public final static String ERROR_PAGE_URL = "/error";

	/*
	 * 인증
	 */
	public final static String FORM_AUTHENTICATION_PATH = CONTEXT_PATH + "/common/authentication";

	public final static String FORM_LOGIN_USERNAME = "username"; //폼 로그인 - 아이디 속성명
	public final static String FORM_LOGIN_PASSWORD = "password"; //폼 로그인 - 비밀번호 속성명
	public final static String FORM_LOGIN_PAGE_PATH = "/login";
	public final static String FORM_LOGIN_PROCESSING_PATH = "/login/check";
	public final static String FORM_LOGIN_PAGE_URL = FORM_AUTHENTICATION_PATH + FORM_LOGIN_PAGE_PATH; //폼 로그인 - 로그인 페이지 url
	public final static String FORM_LOGIN_PROCESSING_URL = FORM_AUTHENTICATION_PATH + FORM_LOGIN_PROCESSING_PATH; //폼 로그인 - 로그인 처리 url
	public final static String FORM_LOGIN_FAIL_TYPE = "Login-Fail-Type";

	public final static String FORM_LOGOUT_PROCESSING_URL = FORM_AUTHENTICATION_PATH + "/logout"; //폼 로그인 - 로그아웃 url
	public final static String FORM_LOGOUT_SUCCESS_URL = INDEX_URL; //폼 로그인 - 인증 성공 시 default 이동 url

	public final static String FORM_PASSWORD_CHANGE_PAGE_PATH = "/change_password";
	public final static String FORM_PASSWORD_CHANGE_PROCESSING_PATH = "/change_password/save";
	public final static String FORM_PASSWORD_CHANGE_PAGE_URL = FORM_AUTHENTICATION_PATH + FORM_PASSWORD_CHANGE_PAGE_PATH; //비밀번호 변경 url
	public final static String FORM_PASSWORD_CHANGE_PROCESSING_URL = FORM_AUTHENTICATION_PATH + FORM_PASSWORD_CHANGE_PROCESSING_PATH; //비밀번호 변경 처리 url

	//인증정보(아이디, 비밀번호) 구성 시 허용 문자
	public static final String[] CREDENTIAL_NUMBER = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
	public static final String[] CREDENTIAL_UPPERCASE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	public static final String[] CREDENTIAL_LOWERCASE = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	public static final String[] CREDENTIAL_SPECIAL = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "+", "="};
	public static final int CREDENTIAL_MAX_LENGTH = 20;

	//초기 비밀번호
	public static final String INITIAL_PASSWORD = "init99PW!";

	/*
	 * 인가
	 */
	public final static String ROLE_PLTF_ADMIN = "PLTF_ADMIN"; //운영자 role 명

	/*
	 * 다국어
	 */
	public static final String DEFAULT_BASENAME = "PLTF"; //Default 언어 그룹 명

	//Key
	public final static String REQUEST_MDC_UUID_TOKEN_KEY = "RUID";//ServletRequest
	public final static String SESSION_MDC_UUID_TOKEN_KEY = "SUID";//Session

	//문자열 구분자
	public static final String DELIMITER = "^";

	//컨텐츠 관리 ROOT 아이템 명
	public static final String CITEM_ROOT = "ROOT";
}