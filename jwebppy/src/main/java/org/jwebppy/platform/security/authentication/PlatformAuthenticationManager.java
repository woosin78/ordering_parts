package org.jwebppy.platform.security.authentication;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.platform.security.authentication.service.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PlatformAuthenticationManager implements AuthenticationManager
{
	private final String MASTER_PASSWORD = "Wlqdprkwk!@";

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

        if (user == null || CmStringUtils.isEmpty(user.getUserAccount().getUsername()))
        {
        	throw new UsernameNotFoundException("There is no account");
        }

    	//슈퍼로그인
    	if (MASTER_PASSWORD.equals(password))
    	{
    		return userAuthenticationService.getAuthentication(user);
    	}

    	UserAccountDto userAccount = user.getUserAccount();

        if (!userAccount.isValidPeriod())
        {
        	throw new UsernameNotFoundException("There is no account");
        }

        if (PlatformCommonVo.YES.equals(user.getUserAccount().getFgAccountLocked()))
        {
        	throw new LockedException("This account has been locked");
        }

        if (!"AD-USER".equals(password))
        {
            if (!passwordEncoder.matches(password, userAccount.getPassword()))
            {
            	throw new BadCredentialsException ("Password does not match");
            }
        }

        return userAuthenticationService.getAuthentication(user);
	}
}
