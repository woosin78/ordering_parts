package org.jwebppy.config;

import java.util.Locale;

import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MessageConfig implements WebMvcConfigurer
{
	@Bean
	public LocaleResolver localeResolver()
	{
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);

		return sessionLocaleResolver;
	}

	@Bean
	public MessageSource messageSource()
	{
		return new I18nMessageSource();
	}

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor()
    {
    	LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    	localeChangeInterceptor.setParamName("lang");

        return localeChangeInterceptor;
    }

	@Override
	public void addInterceptors(InterceptorRegistry interceptorRegistry)
	{
		interceptorRegistry.addInterceptor(localeChangeInterceptor());
	}
}
