package org.jwebppy.platform.core.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class ResponseMessage
{
	public static Map<String, Object> toEmpty()
	{
		return toJson(HttpStatus.OK.value(), "", "");
	}

	public static Map<String, Object> toJson(Object result)
	{
		return toJson(HttpStatus.OK.value(), "", result);
	}

	public static Map<String, Object> toJson(int code, String message, Object result)
	{
		Map<String, Object> returnMap = new HashMap<String, Object>();

		returnMap.put("CODE", code);
		returnMap.put("MESSAGE", message);
		returnMap.put("RESULT", result);

		return returnMap;
	}
}
