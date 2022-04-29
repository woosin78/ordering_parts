package org.jwebppy.platform.core.security.authentication;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.sso.uitils.StringEncrypter;
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

    private final static Map<String, String[]> SSO_ALLOWED_SYSTEMS = new HashMap<>();

    static
    {
    	SSO_ALLOWED_SYSTEMS.put("SSO_DOOBIZ", new String[] {"1-5fd1c5f3-241f-4830-936b-ecd59335bc76", "1-ef6d7d40-a788-4318-8d55-26dc57371ce5"});
    }

    public PlatformAuthenticationFilter() {}

	public PlatformAuthenticationFilter(AuthenticationManager authenticationManager)
	{
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
	{
		String username = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME));
		String password = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_PASSWORD));

		if ("".equals(username))
		{
			throw new UsernameNotFoundException("The username or password is incorrect.");
		}

		if ("".equals(password))
		{
			throw new BadCredentialsException("The username or password is incorrect.");
		}

		if (isAdUser(CmStringUtils.trimToEmpty(request.getParameter("token"))))
		{
			password = "AD-USER";
		}

		if (SSO_ALLOWED_SYSTEMS.containsKey(username))
		{
			username = ssoValidCheck(username, password);

			if (CmStringUtils.isEmpty(username))
			{
				throw new BadCredentialsException("Unauthorized user.");
			}

			password = "SSO-USER";
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

    private String ssoValidCheck(String username, String password)
    {
		try
		{
			String[] secretInfo = SSO_ALLOWED_SYSTEMS.get(username);

			String[] message = CmStringUtils.split(new StringEncrypter(secretInfo[0], secretInfo[1]).decrypt(password), ":");

	    	if (ArrayUtils.isEmpty(message))
	    	{
	    		return null;
	    	}

	    	LocalDateTime time = CmDateTimeUtils.toLocalDateTime(message[1]);

	    	long period = ChronoUnit.SECONDS.between(time, LocalDateTime.now());

	    	if (period > 30)
	    	{
	    		return null;
	    	}

	    	return message[0];
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

    	return null;
    }
}
