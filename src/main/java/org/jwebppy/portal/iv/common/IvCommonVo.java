package org.jwebppy.portal.iv.common;

import org.jwebppy.portal.common.PortalCommonVo;

public class IvCommonVo extends PortalCommonVo
{
	public final static String REQUEST_PATH = PortalCommonVo.REQUEST_PATH + "/iv";

	//EU&UK 딜러 계정 발번 시 아이디에 적용되는 prefix
	public final static String EUUK_USERNAME_PREFIX = "D_";
	
	//Default date format.
	public static final String DEFAULT_DATE_YYYYMMDD_FORMAT = "yyyyMMdd";
	public static final String DEFAULT_TIME_HHMMSS_FORMAT = "HHmmss";
	public static final String DEFAULT_TIME_MMHH_FORMAT = "HH:mm";
}
