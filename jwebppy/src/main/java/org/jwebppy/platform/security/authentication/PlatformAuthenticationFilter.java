package org.jwebppy.platform.security.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
	@Autowired
	private Environment environment;

	public PlatformAuthenticationFilter(AuthenticationManager authenticationManager)
	{
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
	{
		String username = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGON_USERNAME));
		String password = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGON_PASSWORD));

		if ("".equals(username))
		{
			throw new UsernameNotFoundException("The username is empty");
		}

		if ("".equals(password))
		{
			throw new BadCredentialsException("The password is empty");
		}

		String token = CmStringUtils.trimToEmpty(request.getParameter("token"));

		if (!"".equals(token) && isAdUser(token))
		{
			password = "AD-USER";
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

		setDetails(request, usernamePasswordAuthenticationToken);

		return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
	}

    private boolean isAdUser(String token)
    {
    	String secret = environment.getProperty("sso.ad.domainSecret");

        try
        {
            Algorithm algorithm = Algorithm.HMAC256(secret);
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
