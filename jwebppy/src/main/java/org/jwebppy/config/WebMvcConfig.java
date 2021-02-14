package org.jwebppy.config;

import org.jwebppy.platform.filter.PlatformRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
	@Bean
	public FilterRegistrationBean<PlatformRequestFilter> platformRequestFilter()
	{
		FilterRegistrationBean<PlatformRequestFilter> filterRegistrationBean = new FilterRegistrationBean<>();

		filterRegistrationBean.setFilter(new PlatformRequestFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setOrder(Integer.MIN_VALUE + 1);

		return filterRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean()
	{
		FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new XssEscapeServletFilter());
		filterRegistration.setOrder(1);
		filterRegistration.addUrlPatterns("/*");

		return filterRegistration;
	}
}