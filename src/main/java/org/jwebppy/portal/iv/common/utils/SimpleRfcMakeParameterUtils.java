package org.jwebppy.portal.iv.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.util.CmStringUtils;

public class SimpleRfcMakeParameterUtils
{
	//사용자 기본 정보
	public static Map<String, String> me(Map<String, Object> iParamMap)
	{
		Map<String, String> oParamMap = new HashMap<>();

		oParamMap.put("KUNNR", CmStringUtils.trimToEmpty(iParamMap.get("KUNNR")));
		oParamMap.put("VKORG", CmStringUtils.trimToEmpty(iParamMap.get("VKORG")));
		oParamMap.put("VTWEG", CmStringUtils.trimToEmpty(iParamMap.get("VTWEG")));
		oParamMap.put("SPART", CmStringUtils.trimToEmpty(iParamMap.get("SPART")));
		oParamMap.put("STATUS", "X");

		return oParamMap;
	}
}
