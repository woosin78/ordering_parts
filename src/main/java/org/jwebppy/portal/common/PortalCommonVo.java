package org.jwebppy.portal.common;

import org.jwebppy.platform.core.PlatformCommonVo;

public class PortalCommonVo extends PlatformCommonVo
{
	public final static String REQUEST_PATH = PortalConfigVo.CONTEXT_PATH;

	//문자열 구분자
	public static final String DELIMITER = PortalConfigVo.DELIMITER;

	//계정 생성 시 부여되는 초기 비밀번호
	public static final String INITIAL_PASSWORD = "init99PW!";
}
