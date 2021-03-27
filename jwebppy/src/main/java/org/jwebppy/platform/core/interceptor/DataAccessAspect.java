package org.jwebppy.platform.core.interceptor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.jwebppy.platform.core.util.CmAnnotationUtils;
import org.jwebppy.platform.core.util.CmReflectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;

public class DataAccessAspect
{
	//DTO, Entity 의 RegUsername, RegDate, ModUsername, ModDate 값을 자동으로 넣어줌
	protected void injectCommonValue(ProceedingJoinPoint joinPoint)
	{
		String methodName = joinPoint.getSignature().getName();
		boolean isInsertMethod = CmStringUtils.startsWithIgnoreCase(methodName, "insert");
		boolean isUpdateMethod = CmStringUtils.startsWithIgnoreCase(methodName, "update");
		boolean isDeleteMethod = CmStringUtils.startsWithIgnoreCase(methodName, "delete");

		Object[] arguments = joinPoint.getArgs();

		if (arguments != null)
		{
			for (Object argument : arguments)
			{
				if (isInsertMethod)
				{
					setValue(argument, "regUsername", UserAuthenticationUtils.getUsername());
					setValue(argument, "regDate", LocalDateTime.now());
				}

				if (isUpdateMethod || isDeleteMethod)
				{
					setValue(argument, "modUsername", UserAuthenticationUtils.getUsername());
					setValue(argument, "modDate", LocalDateTime.now());
				}
			}
		}
	}

	protected void setValue(Object target, String fieldName, Object value)
	{
		Field field = CmReflectionUtils.findField(target.getClass(), fieldName);

		if (field != null && CmStringUtils.isNotEmpty(value))
		{
			CmReflectionUtils.makeAccessible(field);
			CmReflectionUtils.setField(field, target, value);
		}
	}

	protected boolean hasNoLoggingAnnotation(Signature signature)
	{
		MethodSignature methodSignature = (MethodSignature)signature;

		if (CmAnnotationUtils.isAnnotationDeclaredLocally(NoLogging.class, signature.getDeclaringType()) || CmAnnotationUtils.findAnnotation(methodSignature.getMethod(), NoLogging.class) != null)
		{
			return true;
		}

		return false;
	}
}
;