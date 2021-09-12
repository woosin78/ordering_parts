package org.jwebppy.portal;

public class PortalCommonVo
{
	//Default date format
	public static final String DEFAULT_TIMEZONE = "Asia/Seoul";
	public static final String DEFAULT_DATE_TIME_YYYYMMDDHHMMSS_FORMAT = "yyyyMMddHHmmss";
	public static final String DEFAULT_DATE_YYYYMMDD_FORMAT = "yyyyMMdd";
	public static final String DEFAULT_TIME_HHMMSS_FORMAT = "HHmmss";
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	public static final String DEFAULT_TIME_MMHH_FORMAT = "HH:mm";
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_DATE_TIME_MMHH_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String UNLIMITED_DATE_TIME = "9999-12-31 23:59:59";

	//Number
	public static final int UNLIMITED_POSITIVE_NUMBER = 9999999;
	public static final int UNLIMITED_NEGATIVE_NUMBER = -9999999;

	//flag
	public static final String ALL = "A";
	public static final String YES = "Y";
	public static final String NO = "N";

	public static final String SUCCESS = "S";
	public static final String FAIL = "F";

	//Delimiter to split
	public static final String DELIMITER = "^";

	//Root name of menu tree in content Management
	public static final String ROOT = "ROOT";

	//Default basename of language
	public static final String DEFAULT_BASENAME = "PLTF";

	//Default row-per-page of list
	public static final int DEFAULT_ROW_PER_PAGE = 20;

	//Allowable characters of credential
	public static final String[] CREDENTIAL_NUMBER = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
	public static final String[] CREDENTIAL_UPPERCASE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	public static final String[] CREDENTIAL_LOWERCASE = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	public static final String[] CREDENTIAL_SPECIAL = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "-", "+", "="};
	public static final int CREDENTIAL_MAX_LENGTH = 20;
}
