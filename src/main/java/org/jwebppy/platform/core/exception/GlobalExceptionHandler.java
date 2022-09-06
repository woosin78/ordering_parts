package org.jwebppy.platform.core.exception;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.ClientAbortException;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(Exception.class)
	public Object handleException(HttpServletRequest request, Exception e)
	{
		//Broken Pipe exception 은 Server 단에서는 핸들링 할 수 없으므로 console 및 APM 에 logging 되지 않도록 예외 처리한다.
		//인증, 권한 관련 exception 은 console 및 APM 에 logging 되지 않도록 예외 처리한다.
		if (!(e instanceof ClientAbortException || e instanceof AccessDeniedException || e instanceof AuthenticationException))
		{
			e.printStackTrace();
		}

        if (CmStringUtils.equals("XMLHttpRequest", request.getHeader("X-Requested-With")))
        {
        	return new ResponseEntity<>(ResponseMessage.toJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), ""), HttpStatus.INTERNAL_SERVER_ERROR);
        }

		return new RedirectView(PlatformConfigVo.ERROR_PAGE_URL);
    }
}
