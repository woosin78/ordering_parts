package org.jwebppy.platform.core.security.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.service.UserAuthenticationService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.sso.uitils.StringEncrypter;
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

	@Value("${platform.service}")
	private String PLATFORM_SERVICE;

	@Autowired
	private I18nMessageSource i18nMessageSource;

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

        System.err.println("1. Username:" + username + ", Password:" + password);

        if (CmStringUtils.isEmpty(username) || CmStringUtils.isEmpty(password))
        {
        	throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
        }

        UserDto user = userService.getUserByUsername(username);
        AuthenticationType authenticationType = AuthenticationType.N;
        boolean isValidCredentials = false;

        System.err.println("2. User:" + user);

        //계정이 시스템에 존재하는지 체크
        if (user != null)
        {
        	//슈퍼 로그인 비밀번호
        	if (MASTER_PASSWORD.equals(password))
        	{
        		System.err.println("3. Login by the master password");

        		return userAuthenticationService.getAuthentication(user);
        	}

        	UserAccountDto userAccount = user.getUserAccount();

        	if (CmStringUtils.equalsAny(password, AuthenticationType.A.getUniqueName(), AuthenticationType.S.getUniqueName()))
        	{
        		System.err.println("4. Login by AD, SSO: " + password);

        		authenticationType = (password.equals(AuthenticationType.A.getUniqueName())) ? AuthenticationType.A: AuthenticationType.S;
        		isValidCredentials = true;
        	}
        	else
        	{
        		System.err.println("5. Where login by the DoobizPlus's credentials. FgUsePassword:" + userAccount.getFgNoUsePassword());

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
            			System.err.println("6. Login Success");

            			isValidCredentials = true;
            		}
            		else
            		{
                		if (isDoobizUser(username, password))
                		{
                			System.err.println("7. Doobiz Login Success by Doobiz Authentication");

                			authenticationType = AuthenticationType.D;
                			isValidCredentials = true;
                		}
            		}
            	}
        	}

        	if (isValidCredentials)
        	{
                if (!userAccount.isValidPeriod())
                {
                	System.err.println("9. Account Expired");

                	throw new AccountExpiredException(i18nMessageSource.getMessage("PLTF_M_LOGIN_ACCOUNT_EXPIRED"));
                }
                else if (CmStringUtils.equals(PlatformCommonVo.YES, user.getUserAccount().getFgAccountLocked()))
                {
                	System.err.println("10. Account Locked");

                	throw new LockedException(i18nMessageSource.getMessage("PLTF_M_LOGIN_ACCOUNT_LOCKED"));
                }
                else if (CmStringUtils.equals(PlatformCommonVo.YES, user.getUserAccount().getFgPasswordLocked()))
                {
                	if (CmStringUtils.notEquals(PlatformCommonVo.YES, userAccount.getFgNoUsePassword()))
                	{
                		System.err.println("11. Password Locked");

                		throw new CredentialsExpiredException(i18nMessageSource.getMessage("PLTF_M_LOGIN_PASSWORD_EXPIRED"));
                	}
                }

                System.err.println("12. Login Success");

                return userAuthenticationService.getAuthentication(user, authenticationType);
        	}
        }

        throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
	}

	private boolean isDoobizUser(String username, String password)
	{
		String KEY = "Infracore";
		String IV = "Doosan";

		String protocol = "http";
		String host = "10.249.27.18";//EPQ AP
		int port = 50000;
		String path = "/irj/servlet/prt/portal/prtroot/doobiz.portal.auth.DoobizAuthenticationComponent";

		if (CmStringUtils.equals(PLATFORM_SERVICE, "PRD"))
		{
			host = "10.249.16.182";//EPP AP2
		}

		BufferedReader bufferedReader = null;

        try
		{
        	path += "?token=" + new StringEncrypter(KEY, IV).encrypt(CmStringUtils.upperCase(username) + ":" + password + ":" + System.currentTimeMillis());

        	System.err.println("7.1. Doobiz Login - protocol: " + protocol + ", host:" + host + ", port:" + port + ", path:" + path);

        	URL url = new URL(protocol, host, port, path);
        	HttpURLConnection conn = (HttpURLConnection)url.openConnection();

        	conn.setRequestMethod("GET");

        	System.err.println("7.2. Doobiz Login - try to connect");

        	conn.connect();

        	System.err.println("7.3. Doobiz Login - connection success");

			bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String readLine = null;

			while ((readLine = bufferedReader.readLine()) != null)
			{
				response.append(readLine); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
			}

			bufferedReader.close();
			bufferedReader = null;

			System.err.println("7.4. Doobiz Login Result:" + response.toString());

			if (CmStringUtils.equals(response.toString(), PlatformCommonVo.SUCCESS))
			{
				return true;
			}
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
        finally
        {
        	if (bufferedReader != null)
        	{
        		try
        		{
					bufferedReader.close();
				}
        		catch (IOException e)
        		{
					e.printStackTrace();
				}

        		bufferedReader = null;
        	}
		}

		return false;
	}
}
