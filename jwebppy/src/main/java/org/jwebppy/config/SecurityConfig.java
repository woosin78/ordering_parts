package org.jwebppy.config;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.AjaxAwareAuthenticationEntryPoint;
import org.jwebppy.platform.core.security.authentication.PlatformAuthenticationFilter;
import org.jwebppy.platform.core.security.authentication.PlatformAuthenticationManager;
import org.jwebppy.platform.core.security.authentication.handler.LoginFailureHandler;
import org.jwebppy.platform.core.security.authentication.handler.LoginSuccessHandler;
import org.jwebppy.platform.core.security.authentication.handler.LogoutSuccessHandler;
import org.jwebppy.platform.core.web.CmHttpSessionRequestCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//스프링 시큐리티의 메소드 어노테이션 기반 시큐리티를 활성화 하기 위해서 필요
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
			.antMatcher(PlatformConfigVo.CONTEXT_PATH + "/**")
			.authorizeRequests()
				.antMatchers(PlatformConfigVo.INDEX_URL, PlatformConfigVo.ERROR_PAGE_URL, PlatformConfigVo.FORM_PASSWORD_CHANGE_PAGE_URL, PlatformConfigVo.FORM_PASSWORD_CHANGE_PROCESSING_URL, "/mail/tracking", "/platform/common/authentication/test").permitAll()
				.antMatchers(PlatformConfigVo.CONTEXT_PATH + "/**").hasRole(PlatformConfigVo.ROLE_PLTF_ADMIN)
				.anyRequest().authenticated()
			.and()
			.headers()
				.frameOptions().disable()
			.and()
			.sessionManagement()//세션고정보호. request 시 마다 cookie 의 jsessionid 값을 변경함
				.sessionFixation().changeSessionId()
			.and()
			.logout()
				.logoutUrl(PlatformConfigVo.FORM_LOGOUT_PROCESSING_URL)
				.deleteCookies("JSESSIONID", "J_SESSIONID", "SESSION")
				.logoutSuccessHandler(logoutSuccessHandler())
			.and()
			.addFilterBefore(platformAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.formLogin()
				.loginPage(PlatformConfigVo.FORM_LOGIN_PAGE_URL)
				.permitAll()
			.and()
			.requestCache()
				.requestCache(requestCache())
			.and()
			.exceptionHandling()
				.authenticationEntryPoint(loginUrlAuthenticationEntryPoint());
	}

	@Override
	public void configure(WebSecurity web)
	{
		web
			.ignoring()
				.antMatchers("/static/**", PlatformConfigVo.CONTEXT_PATH + "/css/**", PlatformConfigVo.CONTEXT_PATH + "/js/**", PlatformConfigVo.CONTEXT_PATH + "/img/**", "/favicon.ico");
	}

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SimpleUrlLogoutSuccessHandler logoutSuccessHandler()
	{
		LogoutSuccessHandler logoutSuccessHandler = new LogoutSuccessHandler();
		logoutSuccessHandler.setDefaultTargetUrl(PlatformConfigVo.FORM_LOGOUT_SUCCESS_URL);

		return logoutSuccessHandler;
	}

	@Bean
	public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint()
	{
		return new AjaxAwareAuthenticationEntryPoint(PlatformConfigVo.FORM_LOGIN_PAGE_URL);
	}

    @Bean
    public RequestCache requestCache()
    {
        return new CmHttpSessionRequestCache();
    }

	@Bean
	public PlatformAuthenticationManager platformAuthenticationManager()
	{
	    return new PlatformAuthenticationManager();
	}

	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler(String defaultTargetUrl)
	{
		return new LoginSuccessHandler(PlatformConfigVo.INDEX_URL);
	}

	@Bean
	public AuthenticationFailureHandler loginFailureHandler()
	{
		return new LoginFailureHandler();
	}

    @Bean
    public PlatformAuthenticationFilter platformAuthenticationFilter() throws Exception
    {
    	PlatformAuthenticationFilter platformAuthenticationFilter = new PlatformAuthenticationFilter(platformAuthenticationManager());
    	platformAuthenticationFilter.setFilterProcessesUrl(PlatformConfigVo.FORM_LOGIN_PROCESSING_URL);
    	platformAuthenticationFilter.setUsernameParameter(PlatformConfigVo.FORM_LOGIN_USERNAME);
    	platformAuthenticationFilter.setPasswordParameter(PlatformConfigVo.FORM_LOGIN_PASSWORD);
    	platformAuthenticationFilter.setPostOnly(true);

    	platformAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler(PlatformConfigVo.INDEX_URL));
    	platformAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

    	platformAuthenticationFilter.afterPropertiesSet();

    	return platformAuthenticationFilter;
    }

	@Bean
	public SpringSecurityDialect springSecurityDialect()
	{
		return new SpringSecurityDialect();
	}
}
