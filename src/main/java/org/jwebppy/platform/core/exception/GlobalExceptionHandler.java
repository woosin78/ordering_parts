package org.jwebppy.platform.core.exception;

import javax.servlet.http.HttpServletRequest;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(Exception.class)
	public Object handleException(HttpServletRequest request, Exception e)
	{
        if (CmStringUtils.equals("XMLHttpRequest", request.getHeader("X-Requested-With")))
        {
        	return new ResponseEntity<>(ResponseMessage.toJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

		return new RedirectView(PlatformConfigVo.ERROR_PAGE_URL);
    }
}
