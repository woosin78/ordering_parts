package org.jwebppy.platform.core.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler
{
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest request, Exception e)
//	{
//		e.printStackTrace();
//
//		String message = "[" + HttpStatus.INTERNAL_SERVER_ERROR.toString() + "] " + e.getMessage();
//
//		return new ResponseEntity<>(ResponseMessage.toJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, ""), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//	@ExceptionHandler(Exception.class)
//	public RedirectView handleException(HttpServletRequest request, Exception e)
//	{
//		e.printStackTrace();
//		return new RedirectView(PlatformConfigVo.ERROR_PAGE_URL);
//    }
}
