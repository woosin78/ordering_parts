package org.jwebppy.platform.core.exception;

import javax.servlet.http.HttpServletRequest;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler
{
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest request, Exception e)
//	{
//		e.printStackTrace();
//
//		return new ResponseEntity<>(ResponseMessage.toJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

	@ExceptionHandler(Exception.class)
	public RedirectView handleException(HttpServletRequest request, Exception e)
	{
		return new RedirectView(PlatformConfigVo.ERROR_PAGE_URL);
    }
}
