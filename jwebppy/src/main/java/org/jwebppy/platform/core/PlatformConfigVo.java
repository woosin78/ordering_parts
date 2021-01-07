package org.jwebppy.platform.core;

public class PlatformConfigVo
{
	public final static String INDEX_URL = "/";
	public final static String ERROR_PAGE_URL = "/error";

	//Authentication
	public final static String FORM_LOGIN_USERNAME = "username";
	public final static String FORM_LOGIN_PASSWORD = "password";
	public final static String FORM_LOGIN_PAGE_URL = "/platform/security/authentication/login_form";
	public final static String FORM_LOGIN_PROCESSING_URL = "/platform/security/authentication/check";
	public final static String FORM_LOGOUT_PROCESSING_URL = "/platform/security/authentication/logout";
	public final static String FORM_LOGOUT_SUCCESS_URL = "/";
	public final static String FORM_LOGIN_USERNAME_RULE_PATTERN = "^((?=.*[a-zA-Z])|(?=.*\\d)).{6,12}$";
	public final static String FORM_LOGIN_PASSWORD_RULE_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";
	public final static int FORM_LOGIN_PASSWORD_FAIL_LIMIT_COUNT = 5;

	//Authorization
	public final static String ROLE_SUPER_ADMIN = "PLTF_SUPER_ADMIN";

	//Key
	public final static String REQUEST_MDC_UUID_TOKEN_KEY = "RUID";

	//kinds of DB
	public final static String MariaDB = "mariadb";
	public final static String Oracle = "oracle";

	//Cache
	public static final String LANG = "Language";
	public static final String CITEM = "Content";
	public static final String USER = "User";
	public static final String CUSTOMER = "Customer";
	public static final String ORDER_TYPE = "OrderType";
	public static final String ORDER_DISPLAY = "OrderDisplay";
	public static final String BACKORDER = "Backorder";
	public static final String ORDER_STATUS = "OrderStatus";
	public static final String INVOICE_STATUS = "InvoiceStatus";
	public static final String SHIPMENT_STATUS = "ShipmentStatus";
	public static final String PARTS_INFO_AUTOCOMPLETE = "PartsInfoAutoComplete";

	public static final int TTL_5_MINUTES = 5*60;
	public static final int TTL_10_MINUTES = 10*60;
	public static final int TTL_1_HOUR = 10*60*60;
	public static final int TTL_24_HOURS = 24*60*60;
}
