package org.jwebppy.platform.core.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.service.UserAuthenticationService;
import org.jwebppy.platform.core.util.CmAnnotationUtils;
import org.jwebppy.platform.core.util.CmArrayUtils;
import org.jwebppy.platform.core.util.CmClassUtils;
import org.jwebppy.platform.core.util.CmResponseEntityUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Aspect
@Component
public class RequestMonitorAspect
{
	@Autowired
	private UserAuthenticationService userAuthenticationService;

	private Map<String, String> VIEW_URLS = new HashMap<>();
	private Set<String> TARGETS_TO_RETURN_RESPONSE_ENTITY = new HashSet<>();

	@PostConstruct
	private void init()
	{
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));

		for (BeanDefinition beanDefinition : scanner.findCandidateComponents("org.jwebppy"))
		{
			try
			{
				Class<?> clazz = CmClassUtils.getClass(beanDefinition.getBeanClassName());

				Annotation annotation = CmAnnotationUtils.findAnnotation(clazz, RestController.class);

				if (annotation != null)
				{
					TARGETS_TO_RETURN_RESPONSE_ENTITY.add(clazz.getName());

					continue;
				}

				annotation = CmAnnotationUtils.findAnnotation(clazz, RequestMapping.class);

				if (annotation != null)
				{
					Method[] methods = clazz.getMethods();

					if (CmArrayUtils.isNotEmpty(methods))
					{
						for (Method method : methods)
						{
							if (method.isAnnotationPresent(ResponseBody.class))
							{
								TARGETS_TO_RETURN_RESPONSE_ENTITY.add(clazz.getName() + "." + method.getName());

								continue;
							}

							Annotation[] methodAnnotations = method.getAnnotations();

							if (CmArrayUtils.isNotEmpty(methodAnnotations))
							{
								for (Annotation methodAnnotation : methodAnnotations)
								{
									if (methodAnnotation instanceof RequestMapping ||  methodAnnotation instanceof PostMapping || methodAnnotation instanceof GetMapping)
									{
										VIEW_URLS.put(clazz.getName() + "." + method.getName(), CmStringUtils.trimToEmpty(CmAnnotationUtils.getAnnotationValue(annotation)) + CmStringUtils.trimToEmpty(CmAnnotationUtils.getAnnotationValue(methodAnnotation)));
									}
								}
							}
						}
					}
				}
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Around("execution(* org.jwebppy..*Controller.*(..))")
	public Object onAround(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable
	{
		if (UserAuthenticationUtils.isLogin())
		{
			if (PlatformCommonVo.YES.equals(UserAuthenticationUtils.getUserDetails().getFgPasswordLocked()))
			{
				return "/platform/common/authentication/login_form";
			}
		}

		//사용자 정보 업데이트
		updateUserDetails();

		Object result = proceedingJoinPoint.proceed();

		if (result instanceof ResponseEntity || result instanceof RedirectView)
		{
			return result;
		}

		if (isTargetToReturnResponseEntity(proceedingJoinPoint.getSignature()))
		{
			return CmResponseEntityUtils.responseEntity(result);
		}

		return getViewUrl(proceedingJoinPoint.getSignature(), result);
	}

	private boolean isTargetToReturnResponseEntity(Signature signature)
	{
		if (TARGETS_TO_RETURN_RESPONSE_ENTITY.contains(signature.getDeclaringTypeName()) || TARGETS_TO_RETURN_RESPONSE_ENTITY.contains(signature.getDeclaringTypeName() + "." + ((MethodSignature)signature).getMethod().getName()))
		{
			return true;
		}

		return false;
	}

	private String getViewUrl(Signature signature, Object returnValue)
	{
		if (!"DUMMY".equals(CmStringUtils.trimToEmpty(returnValue)))
		{
			return CmStringUtils.trimToEmpty(returnValue);
		}

		return VIEW_URLS.get(signature.getDeclaringTypeName() + "." + ((MethodSignature)signature).getMethod().getName());
	}

	private void updateUserDetails()
	{
		if (UserAuthenticationUtils.isLogin())
		{
			SecurityContextHolder.getContext().setAuthentication(userAuthenticationService.getAuthentication(UserAuthenticationUtils.getUsername()));
		}
	}
}
