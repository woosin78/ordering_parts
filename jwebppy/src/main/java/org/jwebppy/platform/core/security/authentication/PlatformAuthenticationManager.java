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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

        UserDto user = userService.getUserByUsername(username);
        boolean isValidCredentials = false;

        System.err.println("===================1:" + user);

        //계정이 시스템에 존재하는지 체크
        if (user != null && CmStringUtils.isNotEmpty(user.getUserAccount().getUsername()))
        {
        	//throw new UsernameNotFoundException("The username or password you entered is incorrect.");

            System.err.println("===================2:" + isValidCredentials);

        	//슈퍼 로그인 비밀번호
        	if (MASTER_PASSWORD.equals(password))
        	{
        		return userAuthenticationService.getAuthentication(user);
        	}

        	System.err.println("===================3:" + isValidCredentials);

        	UserAccountDto userAccount = user.getUserAccount();

        	System.err.println("userAccount.getFgNoUsePassword():" + userAccount.getFgNoUsePassword());

        	/*
        	 * 자체 계정 인증 허용 여부
        	 * Y: 인증 허용 않음
        	 * N: 인증 허용
        	 */
        	if (PlatformCommonVo.NO.equals(userAccount.getFgNoUsePassword()))
        	{
        		if (passwordEncoder.matches(password, userAccount.getPassword()))
        		{
        			//throw new BadCredentialsException("The username or password you entered is incorrect.");
        			isValidCredentials = true;
        		}
        	}

        	System.err.println("===================4:" + isValidCredentials);

            //LDAP 인증
        	if (!isValidCredentials && LDAP_ENABLE)
            {
                if (isValidUserByLdap(username, password))
                {
                	//throw new BadCredentialsException("The username or password you entered is incorrect.");
                	isValidCredentials = true;
                }
            }

        	System.err.println("===================5:" + isValidCredentials);

        	if (isValidCredentials)
        	{
                if (!userAccount.isValidPeriod())
                {
                	throw new UsernameNotFoundException("The username or password you entered is incorrect.");
                }

                if (PlatformCommonVo.YES.equals(user.getUserAccount().getFgAccountLocked()))
                {
                	throw new LockedException("The account has been locked");
                }

                return userAuthenticationService.getAuthentication(user);
        	}
        	else
        	{
        		throw new BadCredentialsException("The username or password you entered is incorrect.");
        	}
        }

        return null;

    	/*
        if (!passwordEncoder.matches(password, userAccount.getPassword()))
        {
            //LDAP 인증
        	if (LDAP_ENABLE)
            {
                if (!isValidUserByLdap(username, password))
                {
                	throw new BadCredentialsException("The username or password you entered is incorrect.");
                }
            }
            else
            {
            	throw new BadCredentialsException("The username or password you entered is incorrect.");
            }
        }

        if (!userAccount.isValidPeriod())
        {
        	throw new UsernameNotFoundException("The username or password you entered is incorrect.");
        }

        if (PlatformCommonVo.YES.equals(user.getUserAccount().getFgAccountLocked()))
        {
        	throw new LockedException("The account has been locked");
        }

        return userAuthenticationService.getAuthentication(user);
        */
	}

	private boolean isValidUserByLdap(String username, String password)
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
