package org.jwebppy.config;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.web.CmHttpSessionRequestCache;
import org.jwebppy.platform.security.authentication.AjaxAwareAuthenticationEntryPoint;
import org.jwebppy.platform.security.authentication.PlatformAuthenticationFilter;
import org.jwebppy.platform.security.authentication.PlatformAuthenticationManager;
import org.jwebppy.platform.security.authentication.handler.LoginFailureHandler;
import org.jwebppy.platform.security.authentication.handler.LoginSuccessHandler;
import org.jwebppy.platform.security.authentication.handler.LogoutSuccessHandler;
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
				.antMatchers("/", "/error", "/sso/inbound/**").permitAll()
				.antMatchers(
						"/platform/security/authentication/check_password_expired",
						"/platform/security/authentication/last_login_info",
						"/platform/security/session/refresh").authenticated()
				.antMatchers("/platform/**").hasRole(PlatformConfigVo.ROLE_SUPER_ADMIN)
				.anyRequest().authenticated()
			.and()
			.headers()
				.frameOptions().disable()
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
				.loginPage(PlatformConfigVo.FORM_LOGON_PAGE_URL)
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
				.antMatchers("/static/**", "/platform/css/**", "/platform/js/**", "/platform/img/**", "/portal/css/**", "/portal/js/**", "/portal/img/**", "/portal/manual/**", "/favicon.ico");
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
		return new AjaxAwareAuthenticationEntryPoint(PlatformConfigVo.FORM_LOGON_PAGE_URL);
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
    	platformAuthenticationFilter.setFilterProcessesUrl(PlatformConfigVo.FORM_LOGON_PROCESSING_URL);
    	platformAuthenticationFilter.setUsernameParameter(PlatformConfigVo.FORM_LOGON_USERNAME);
    	platformAuthenticationFilter.setPasswordParameter(PlatformConfigVo.FORM_LOGON_PASSWORD);
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
