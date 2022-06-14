package org.jwebppy.platform.core.security.authentication;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.sso.uitils.StringEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class PlatformAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
	@Value("${sso.ad.domainSecret}")
	private String DOMAIN_SECRET;

	@Autowired
	private I18nMessageSource i18nMessageSource;

    private final static Map<String, String[]> SSO_ALLOWED_SYSTEMS = new HashMap<>();

    static
    {
    	SSO_ALLOWED_SYSTEMS.put("DIV_DOOBIZ", new String[] {"Infracore", "Doosan"});
    	SSO_ALLOWED_SYSTEMS.put("GPES", new String[] {"GPES", "everythingisok"});
    }

    public PlatformAuthenticationFilter() {}

	public PlatformAuthenticationFilter(AuthenticationManager authenticationManager)
	{
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
	{
		String username = null;
		String password = null;

		String[] credentials = checkSsoUser(request);

		if (ArrayUtils.isNotEmpty(credentials))
		{
			username = credentials[0];
			password = credentials[1];
		}
		else
		{
			username = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME));
			password = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_PASSWORD));

			if ("".equals(username))
			{
				throw new UsernameNotFoundException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
			}

			if ("".equals(password))
			{
				throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
			}

			if (isAdUser(CmStringUtils.trimToEmpty(request.getParameter("token"))))
			{
				password = AuthenticationType.A.getUniqueName();
			}
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

		setDetails(request, usernamePasswordAuthenticationToken);

		return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
	}

    private boolean isAdUser(String token)
    {
    	if (CmStringUtils.isEmpty(token))
    	{
    		return false;
    	}

        try
        {
            Algorithm algorithm = Algorithm.HMAC256(DOMAIN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("DOOSAN").build();

            DecodedJWT jwt = verifier.verify(token);

            System.err.println("token:" + token);
            System.err.println("jwt:" + jwt.toString());
            System.err.println("result:" + ("DOOSAN-SSO-AUTH".equals(jwt.getSubject())));

            if("DOOSAN-SSO-AUTH".equals(jwt.getSubject()))
            {
            	return true;
            }
        }
        catch (JWTVerificationException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private String[] checkSsoUser(HttpServletRequest request)
    {
    	String system = CmStringUtils.trimToEmpty(request.getParameter("system"));
    	String key = CmStringUtils.trimToEmpty(request.getParameter("key"));

    	if (CmStringUtils.isAnyEmpty(system, key))
    	{
    		return null;
    	}

		try
		{
			String[] secretInfo = SSO_ALLOWED_SYSTEMS.get(system);

			String[] message = CmStringUtils.split(new StringEncrypter(secretInfo[0], secretInfo[1]).decrypt(key), ":");

	    	if (ArrayUtils.isNotEmpty(message) && message.length == 2 && isValidPeriod(message[1]))
	    	{
	    		return new String[] {message[0], AuthenticationType.S.getUniqueName()};
	    	}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

    	return null;
    }

	public boolean isValidPeriod(String time)
	{
		try
		{
			LocalDateTime ssoTime = CmDateTimeUtils.toLocalDateTime(time, PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS);

			if (ssoTime.plusHours(12).compareTo(LocalDateTime.now()) > 0)
			{
				return true;
			}
		}
		catch (DateTimeParseException e) {}

		return false;
	}
}
