package org.jwebppy.platform.core.util;

import java.util.Map;

import org.jwebppy.platform.core.web.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public class CmResponseEntityUtils
{
	public static <T> ResponseEntity<Map<String, Object>> responseEntity(@Nullable T body)
	{
		return responseEntity(body, HttpStatus.OK);
	}

	public static <T> ResponseEntity<Map<String, Object>> responseEntity(@Nullable T body, HttpStatus status)
	{
		return responseEntity(ResponseMessage.toJson(body), HttpStatus.OK);
	}

	public static <T> ResponseEntity<Map<String, Object>> responseEntity(Map<String, Object> responseMap, HttpStatus status)
	{
		return new ResponseEntity<>(responseMap, status);
	}

	public static <T> ResponseEntity<Map<String, Object>> empty()
	{
		return new ResponseEntity<>(ResponseMessage.toEmpty(), HttpStatus.OK);
	}
}
