package org.jwebppy.platform.core.security.authentication;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.service.UserAuthenticationService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PlatformAuthenticationManager implements AuthenticationManager
{
	@Value("${master.password}")
	private String MASTER_PASSWORD;

	@Value("${ldap.enable}")
	private boolean LDAP_ENABLE;

	@Value("${ldap.url}")
	private String LDAP_URL;

	@Value("${ldap.dn}")
	private String LDAP_DEFAULT_NAMING_CONTEXT;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private UserAuthenticationService userAuthenticationService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        if (CmStringUtils.isEmpty(username) || CmStringUtils.isEmpty(password))
        {
        	throw new BadCredentialsException("The username or password is incorrect.");
        }

        UserDto user = userService.getUserByUsername(username);
        boolean isValidCredentials = false;

        //계정이 시스템에 존재하는지 체크
        if (user != null)
        {
        	//슈퍼 로그인 비밀번호
        	if (MASTER_PASSWORD.equals(password))
        	{
        		return userAuthenticationService.getAuthentication(user);
        	}

        	UserAccountDto userAccount = user.getUserAccount();

        	/*
        	 * 자체 계정 인증 허용 여부
        	 * Y: 인증 허용 않음
        	 * N: 인증 허용
        	 */
        	if (PlatformCommonVo.NO.equals(userAccount.getFgNoUsePassword()))
        	{
        		if (passwordEncoder.matches(password, userAccount.getPassword()))
        		{
        			isValidCredentials = true;
        		}
        	}

            //LDAP 인증
        	if (!isValidCredentials && LDAP_ENABLE)
            {
                if (isValidUserByLdap(username, password))
                {
                	isValidCredentials = true;
                }
            }

        	if (isValidCredentials)
        	{
                if (!userAccount.isValidPeriod())
                {
                	throw new AccountExpiredException("The account has expired.");
                }
                else if (PlatformCommonVo.YES.equals(user.getUserAccount().getFgAccountLocked()))
                {
                	throw new LockedException("The account has been locked.");
                }
                else if (PlatformCommonVo.YES.equals(user.getUserAccount().getFgPasswordLocked()))
                {
                	throw new CredentialsExpiredException("The password has expired.");
                }

                return userAuthenticationService.getAuthentication(user);
        	}
        }

        throw new BadCredentialsException("The username or password is incorrect.");
	}

	protected boolean isValidUserByLdap(String username, String password)
	{
		try
		{
			Hashtable<String, String> validateEnv = new Hashtable<>();
			validateEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			validateEnv.put(Context.PROVIDER_URL, LDAP_URL);
			validateEnv.put(Context.SECURITY_PRINCIPAL, "DSG\\" + username);
			validateEnv.put(Context.SECURITY_CREDENTIALS, password);

			DirContext ctx = new InitialDirContext(validateEnv);
			ctx.getAttributes(LDAP_DEFAULT_NAMING_CONTEXT);
			ctx.close();
		}
		catch (AuthenticationException e)
		{
			return false;
		}
		catch (NamingException e)
		{
			return false;
		}

		return true;
	}
}
