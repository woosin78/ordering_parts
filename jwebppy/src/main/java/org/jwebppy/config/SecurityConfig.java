package org.jwebppy.config;

import org.jwebppy.platform.core.web.CmHttpSessionRequestCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//스프링 시큐리티의 메소드 어노테이션 기반 시큐리티를 활성화 하기 위해서 필요
@EnableRedisHttpSession
public class SecurityConfig
{
	@Bean
    public RequestCache requestCache()
    {
        return new CmHttpSessionRequestCache();
    }

	@Bean
	public SpringSecurityDialect springSecurityDialect()
	{
		return new SpringSecurityDialect();
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
