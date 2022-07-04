package org.jwebppy.platform.core.interceptor;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jwebppy.platform.core.dto.ServletRequestInfoDto;
import org.jwebppy.platform.core.util.JdbcStatementContextUtils;
import org.jwebppy.platform.core.util.SessionContextUtils;
import org.jwebppy.platform.core.util.ThreadLocalContextUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class JdbcExecutionAspect extends DataAccessAspect
{
	@Value("${platform.db}")
	private String db;

	@Value("${if.logging.db}")
	private boolean isActiveLogging;

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
			if (isActiveLogging)
			{
				Signature signature = proceedingJoinPoint.getSignature();

				if (!hasNoLoggingAnnotation(signature))
				{
					DataAccessLogDto dataAccessLog = JdbcStatementContextUtils.get();

					if (ObjectUtils.isNotEmpty(dataAccessLog))
					{
						ServletRequestInfoDto servletRequestInfo = ThreadLocalContextUtils.servletRequestInfo.get();

						dataAccessLog.setClassName(signature.getDeclaringTypeName());
						dataAccessLog.setMethodName(signature.getName());
						dataAccessLog.setRequestId(servletRequestInfo.getRequestId());
						dataAccessLog.setRequestUri(servletRequestInfo.getRequestUri());
						dataAccessLog.setReferer(servletRequestInfo.getReferer());
						dataAccessLog.setSessionId(SessionContextUtils.getSessionId());
						dataAccessLog.setError(error);

						if (UserAuthenticationUtils.isAuthenticated())
						{
							dataAccessLog.setTimezone(UserAuthenticationUtils.getUserDetails().getTimezone());
							dataAccessLog.setRegUsername(UserAuthenticationUtils.getUsername());
						}

						dataAccessLogService.writeLog(dataAccessLog);
					}
				}
			}
		}

		return result;
	}
}
