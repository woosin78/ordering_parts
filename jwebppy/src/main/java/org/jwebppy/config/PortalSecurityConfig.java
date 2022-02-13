package org.jwebppy.config;

import org.jwebppy.platform.core.security.authentication.PlatformAuthenticationManager;
import org.jwebppy.platform.core.security.authentication.PortalAuthenticationEntryPoint;
import org.jwebppy.platform.core.security.authentication.PortalAuthenticationFilter;
import org.jwebppy.platform.core.security.authentication.handler.PortalLoginFailureHandler;
import org.jwebppy.platform.core.security.authentication.handler.PortalLoginSuccessHandler;
import org.jwebppy.platform.core.security.authentication.handler.PortalLogoutSuccessHandler;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
@Order(2)
public class PortalSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private RequestCache requestCache;

	@Autowired
	private PlatformAuthenticationManager authenticationManager;

	private final String[] permitAllRequests = {
			PortalConfigVo.INDEX_URL,
			PortalConfigVo.ERROR_PAGE_URL,
			PortalConfigVo.FORM_PASSWORD_CHANGE_PAGE_URL,
			PortalConfigVo.FORM_PASSWORD_CHANGE_PROCESSING_URL
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
			.antMatcher(PortalConfigVo.CONTEXT_PATH + "/**")
			.authorizeRequests()
				.antMatchers(permitAllRequests).permitAll()
				.anyRequest().authenticated()
			.and()
			.headers()
				.frameOptions().disable()
			.and()
			.sessionManagement()//세션고정보호. request 시 마다 cookie 의 jsessionid 값을 변경함
				.sessionFixation().changeSessionId()
				.maximumSessions(1).and().invalidSessionUrl(PortalConfigVo.ERROR_PAGE_URL)
			.and()
			.logout()
				.logoutUrl(PortalConfigVo.FORM_LOGOUT_PROCESSING_URL)
				.deleteCookies("JSESSIONID", "J_SESSIONID", "SESSION")
				.logoutSuccessHandler(logoutSuccessHandler())
			.and()
			.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.formLogin()
				.loginPage(PortalConfigVo.FORM_LOGIN_PAGE_URL)
				.permitAll()
			.and()
			.requestCache()
				.requestCache(requestCache)
			.and()
			.exceptionHandling()
				.authenticationEntryPoint(loginUrlAuthenticationEntryPoint());
	}

	@Override
	public void configure(WebSecurity web)
	{
		web
			.ignoring()
				.antMatchers("/static/**", PortalConfigVo.CONTEXT_PATH + "/css/**", PortalConfigVo.CONTEXT_PATH + "/js/**", PortalConfigVo.CONTEXT_PATH + "/img/**", "/favicon.ico");
	}

	@Bean(name = "portalLoginUrlAuthenticationEntryPoint")
	public PortalAuthenticationEntryPoint loginUrlAuthenticationEntryPoint()
	{
		return new PortalAuthenticationEntryPoint(PortalConfigVo.FORM_LOGIN_PAGE_URL);
	}

	@Bean(name = "portalLoginSuccessHandler")
	public PortalLoginSuccessHandler loginSuccessHandler()
	{
		return new PortalLoginSuccessHandler(PortalConfigVo.INDEX_URL);
	}

	@Bean(name = "portalLoginFailureHandler")
	public PortalLoginFailureHandler loginFailureHandler()
	{
		return new PortalLoginFailureHandler(PortalConfigVo.FORM_LOGIN_PAGE_URL);
	}

	@Bean(name = "portalLogoutSuccessHandler")
	public PortalLogoutSuccessHandler logoutSuccessHandler()
	{
		PortalLogoutSuccessHandler logoutSuccessHandler = new PortalLogoutSuccessHandler();
		logoutSuccessHandler.setDefaultTargetUrl(PortalConfigVo.FORM_LOGOUT_SUCCESS_URL);

		return logoutSuccessHandler;
	}

	@Bean(name = "portalAuthenticationFilter")
    public PortalAuthenticationFilter authenticationFilter() throws Exception
    {
		PortalAuthenticationFilter portalAuthenticationFilter = new PortalAuthenticationFilter();
    	portalAuthenticationFilter.setFilterProcessesUrl(PortalConfigVo.FORM_LOGIN_PROCESSING_URL);
    	portalAuthenticationFilter.setAuthenticationManager(authenticationManager);
    	portalAuthenticationFilter.setUsernameParameter(PortalConfigVo.FORM_LOGIN_USERNAME);
    	portalAuthenticationFilter.setPasswordParameter(PortalConfigVo.FORM_LOGIN_PASSWORD);
    	portalAuthenticationFilter.setPostOnly(true);

    	portalAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    	portalAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

    	portalAuthenticationFilter.afterPropertiesSet();

    	return portalAuthenticationFilter;
    }
}
