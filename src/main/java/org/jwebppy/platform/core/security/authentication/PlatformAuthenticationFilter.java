package org.jwebppy.platform.core.security.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
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
}
