package org.jwebppy.config;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.PlatformAuthenticationEntryPoint;
import org.jwebppy.platform.core.security.authentication.PlatformAuthenticationFilter;
import org.jwebppy.platform.core.security.authentication.PlatformAuthenticationManager;
import org.jwebppy.platform.core.security.authentication.handler.LoginFailureHandler;
import org.jwebppy.platform.core.security.authentication.handler.LoginSuccessHandler;
import org.jwebppy.platform.core.security.authentication.handler.LogoutSuccessHandler;
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
@Order(1)
public class PlatformSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private RequestCache requestCache;

	private final String[] permitAllRequests = {
			PlatformConfigVo.INDEX_URL,
			PlatformConfigVo.ERROR_PAGE_URL,
			PlatformConfigVo.FORM_PASSWORD_CHANGE_PAGE_URL,
			PlatformConfigVo.FORM_PASSWORD_CHANGE_PROCESSING_URL,
			"/mail/tracking"
	};

	private final String[] commonRequests = {
			PlatformConfigVo.CONTEXT_PATH + "/mgmt/gnb/menu", //GNB 메뉴
			PlatformConfigVo.CONTEXT_PATH + "/mgmt/user/login/history/last_login_info" //최종로그인정보 제공
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
			.antMatcher(PlatformConfigVo.CONTEXT_PATH + "/**")
			.authorizeRequests()
				.antMatchers(permitAllRequests).permitAll()
				.antMatchers(commonRequests).authenticated()
				.antMatchers(PlatformConfigVo.CONTEXT_PATH + "/**").hasRole(PlatformConfigVo.ROLE_PLTF_ADMIN)
				//.anyRequest().authenticated()
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
				.antMatchers("/static/**", PlatformConfigVo.CONTEXT_PATH + "/css/**", PlatformConfigVo.CONTEXT_PATH + "/js/**", PlatformConfigVo.CONTEXT_PATH + "/img/**", "/favicon.ico");
	}

	@Bean
	public PlatformAuthenticationEntryPoint loginUrlAuthenticationEntryPoint()
	{
		return new PlatformAuthenticationEntryPoint(PlatformConfigVo.FORM_LOGIN_PAGE_URL);
	}

	@Bean
	public LoginSuccessHandler loginSuccessHandler()
	{
		return new LoginSuccessHandler(PlatformConfigVo.INDEX_URL);
	}

	@Bean
	public LoginFailureHandler loginFailureHandler()
	{
		return new LoginFailureHandler(PlatformConfigVo.FORM_LOGIN_PAGE_URL);
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler()
	{
		LogoutSuccessHandler logoutSuccessHandler = new LogoutSuccessHandler();
		logoutSuccessHandler.setDefaultTargetUrl(PlatformConfigVo.FORM_LOGOUT_SUCCESS_URL);

		return logoutSuccessHandler;
	}

    @Bean
	public PlatformAuthenticationManager platformAuthenticationManager()
	{
	    return new PlatformAuthenticationManager();
	}

	@Bean
    public PlatformAuthenticationFilter platformAuthenticationFilter() throws Exception
    {
    	PlatformAuthenticationFilter platformAuthenticationFilter = new PlatformAuthenticationFilter();
    	platformAuthenticationFilter.setFilterProcessesUrl(PlatformConfigVo.FORM_LOGIN_PROCESSING_URL);
    	platformAuthenticationFilter.setAuthenticationManager(platformAuthenticationManager());
    	platformAuthenticationFilter.setUsernameParameter(PlatformConfigVo.FORM_LOGIN_USERNAME);
    	platformAuthenticationFilter.setPasswordParameter(PlatformConfigVo.FORM_LOGIN_PASSWORD);
    	platformAuthenticationFilter.setPostOnly(true);

    	platformAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    	platformAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

    	platformAuthenticationFilter.afterPropertiesSet();

    	return platformAuthenticationFilter;
    }
}
