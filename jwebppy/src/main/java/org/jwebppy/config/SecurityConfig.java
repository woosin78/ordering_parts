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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
			.authorizeRequests()
				.antMatchers(PlatformConfigVo.INDEX_URL, PlatformConfigVo.ERROR_PAGE_URL, PlatformConfigVo.FORM_PASSWORD_CHANGE_PAGE_URL, PlatformConfigVo.FORM_PASSWORD_CHANGE_PROCESSING_URL, "/mail/tracking").permitAll()
				.antMatchers("/platform/common/**").authenticated()
				.antMatchers("/platform/**").hasRole(PlatformConfigVo.ROLE_PLTF_ADMIN)
				.anyRequest().authenticated()
			.and()
			.headers()
				.frameOptions().disable()
			.and()
			.sessionManagement()
				.sessionFixation().migrateSession()
			.and()
			.csrf().disable()
			.logout()
				.logoutUrl(PlatformConfigVo.FORM_LOGOUT_PROCESSING_URL)
				.invalidateHttpSession(true)
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
				.antMatchers("/static/**", "/platform/css/**", "/platform/js/**", "/platform/img/**", "/favicon.ico");
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
	public AuthenticationSuccessHandler loginSuccessHandler()
	{
		return new LoginSuccessHandler();
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

    	platformAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    	platformAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

    	platformAuthenticationFilter.afterPropertiesSet();

    	return platformAuthenticationFilter;
    }

	@Bean
	public SpringSecurityDialect securityDialect()
	{
		return new SpringSecurityDialect();
	}
}
