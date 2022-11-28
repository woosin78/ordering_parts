package org.jwebppy.platform.core.security.authentication;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.service.UserAuthenticationService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PlatformAuthenticationManager implements AuthenticationManager
{
	private Logger logger = LoggerFactory.getLogger(PlatformAuthenticationManager.class);

	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private UserAuthenticationService userAuthenticationService;

	/*
	 * 계정 관련 Exception 종류
	 * - UsernameNotFoundException: 계정 없음
	 * - BadCredentialsException: 비밀번호 불일치
	 * - AccountStatusException
	 *   ├ AccountExpiredException: 계정만료
	 *   ├ CredentialsExpiredException: 비밀번호 만료
	 *   ├ DisabledException: 계정 비활성화
	 *   └ LockedException: 계정 잠김
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        logger.info("1. Username:" + username + ", Password:" + password);

        if (CmStringUtils.isAnyEmpty(username, password))
        {
        	throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
        }

        String username2 = null;
        String usernameDelimeter = "@";
        if (CmStringUtils.contains(username, usernameDelimeter))
        {
        	String arr[] = CmStringUtils.split(username, usernameDelimeter);

        	username = CmStringUtils.upperCase(arr[0]);
        	username2 = CmStringUtils.upperCase(arr[1]);
        }

        UserDto user = userService.getUserByUsername(username);

        if (ObjectUtils.isEmpty(user))
        {
        	throw new UsernameNotFoundException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
        }

        AuthenticationType authenticationType = checkAuthentication(user, username, password);

        if (ObjectUtils.isNotEmpty(authenticationType))
        {
        	logger.info("13. Username: " + username);
        	logger.info("14. Username2: " + username2);

            if (CmStringUtils.isNotEmpty(username2) && hasSuperLoginAuthority(username))
            {
            	UserDto user2 = userService.getUserByUsername(username2);

            	logger.info("15. user2: " + user2);

            	if (ObjectUtils.isNotEmpty(user2))
            	{
                	if (user2.getUserAccount().isValid())
                	{
                		logger.info("16. Login Success");

                		return userAuthenticationService.getAuthentication(user2, AuthenticationType.F, username);
                	}
            	}
            }
            else
            {
            	return userAuthenticationService.getAuthentication(user, authenticationType);
            }
        }

        throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
	}

	private AuthenticationType checkAuthentication(UserDto user, String username, String password) throws AuthenticationException
	{
        AuthenticationType authenticationType = AuthenticationType.N;
        boolean isValidCredentials = false;

        logger.info("2. User:" + user);

        //계정이 시스템에 존재하는지 체크
        if (ObjectUtils.isNotEmpty(user))
        {
        	UserAccountDto userAccount = user.getUserAccount();

        	if (CmStringUtils.equalsAny(password, AuthenticationType.A.getUniqueName(), AuthenticationType.S.getUniqueName()))
        	{
        		logger.info("4. Login by AD, SSO: " + password);

        		authenticationType = (password.equals(AuthenticationType.A.getUniqueName())) ? AuthenticationType.A: AuthenticationType.S;
        		isValidCredentials = true;
        	}
        	else
        	{
        		logger.info("5. Where login by the DoobizPlus's credentials. FgUsePassword:" + userAccount.getFgNoUsePassword());

            	/*
            	 * DoobizPlus 자체 계정 인증 허용 여부
            	 * Y: 인증 허용 않음
            	 * N: 인증 허용
            	 */
            	if (CmStringUtils.equals(userAccount.getFgNoUsePassword(), PlatformCommonVo.NO))
            	{
            		//AD 인증에 성공했거나 비밀번호가 동일할 경우
            		if (passwordEncoder.matches(password, userAccount.getPassword()))
            		{
            			logger.info("6. Login Success");

            			isValidCredentials = true;
            		}
            	}
        	}

        	if (isValidCredentials)
        	{
                if (!userAccount.isValidPeriod())
                {
                	logger.info("9. Account Expired");

                	throw new AccountExpiredException(i18nMessageSource.getMessage("PLTF_M_LOGIN_ACCOUNT_EXPIRED"));
                }
                else if (CmStringUtils.equals(PlatformCommonVo.YES, user.getUserAccount().getFgAccountLocked()))
                {
                	logger.info("10. Account Locked");

                	throw new LockedException(i18nMessageSource.getMessage("PLTF_M_LOGIN_ACCOUNT_LOCKED"));
                }
                else if (CmStringUtils.equals(PlatformCommonVo.YES, user.getUserAccount().getFgPasswordLocked()))
                {
                	if (CmStringUtils.notEquals(PlatformCommonVo.YES, userAccount.getFgNoUsePassword()))
                	{
                		logger.info("11. Password Locked");

                		throw new CredentialsExpiredException(i18nMessageSource.getMessage("PLTF_M_LOGIN_PASSWORD_EXPIRED"));
                	}
                }

                logger.info("12. Login Success");

                return authenticationType;
        	}
        }

		return null;
	}

	private boolean hasSuperLoginAuthority(String username)
	{
		if (CmStringUtils.isEmpty(username))
		{
			return false;
		}

    	CItemSearchDto cItemSearch = new CItemSearchDto();
    	cItemSearch.setUsername(username);
    	cItemSearch.setType(CItemType.G);

    	for (CItemDto cItem: ListUtils.emptyIfNull(contentAuthorityService.getMyCItems(cItemSearch)))
    	{
    		if (CmStringUtils.equals(cItem.getName(), "DP_SUPER_LOGIN"))
    		{
    			return true;
    		}
    	}

		return false;
	}
}
