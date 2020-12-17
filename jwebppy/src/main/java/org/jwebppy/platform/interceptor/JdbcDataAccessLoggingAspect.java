package org.jwebppy.platform.interceptor;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.JdbcStatementContextUtils;
import org.jwebppy.platform.core.util.SessionContextUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class JdbcDataAccessLoggingAspect extends DataAccessAspect
{
	@Autowired
	private DataAccessLogService dataAccessLogService;

	@Around("execution(* org.jwebppy..mapper.*Mapper.*(..))")
	public Object onAround(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable
	{
		injectCommonValue(proceedingJoinPoint);

		Object result = null;
		String error = null;

		try
		{
			result = proceedingJoinPoint.proceed();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			error = ExceptionUtils.getStackTrace(e);
		}
		finally
		{
			Signature signature = proceedingJoinPoint.getSignature();

			if (!hasNoLoggingAnnotation(signature))
			{
				DataAccessLogDto dataAccessLog = JdbcStatementContextUtils.get();

				if (dataAccessLog != null)
				{
					dataAccessLog.setClassName(signature.getDeclaringTypeName());
					dataAccessLog.setMethodName(signature.getName());
					dataAccessLog.setRequestId(MDC.get(PlatformConfigVo.REQUEST_MDC_UUID_TOKEN_KEY));
					dataAccessLog.setSessionId(SessionContextUtils.getSessionId());
					dataAccessLog.setError(error);
					dataAccessLog.setRegUsername(UserAuthenticationUtils.getUsername());

					dataAccessLogService.writeLog(dataAccessLog);
				}
			}
		}

		return result;
	}
}
