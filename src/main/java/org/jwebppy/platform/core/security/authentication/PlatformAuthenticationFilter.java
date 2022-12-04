package org.jwebppy.platform.core.security.authentication;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
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

		if (CmStringUtils.isEmpty(username))
		{
			throw new UsernameNotFoundException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
		}

		if (CmStringUtils.isEmpty(password))
		{
			throw new BadCredentialsException(i18nMessageSource.getMessage("PLTF_M_LOGIN_AUTHENTICATION_FAILED"));
		}

		if (isAdUser(CmStringUtils.trimToEmpty(request.getParameter("token"))))
		{
			password = AuthenticationType.A.getUniqueName();
		}

		AccountLockedReason accountLockedReason = (AccountLockedReason)request.getSession().getAttribute("ACCOUNT_LOCKED_REASON");

		if (ObjectUtils.isNotEmpty(accountLockedReason))
		{
			CredentialsPolicyDto credentialsPolicy = accountLockedReason.getCredentialsPolicy();

			if (CmStringUtils.equals(PlatformCommonVo.YES, credentialsPolicy.getFgUsePwdFailPenalty()))
			{
				Duration duration = Duration.between(LocalDateTime.now(), accountLockedReason.getLoginFreezingBy());

				long freezingTime = (long)Math.ceil(duration.getSeconds() / 60);

				if (freezingTime > 0)
				{
					throw new AllowableCredentialsFailureException(i18nMessageSource.getMessage("PLTF_M_EXCEEDED_ALLOWABLE_FAILURE_COUNT", new Object[] { credentialsPolicy.getPallowableFailCount(), freezingTime}));
				}
				else
				{
					request.getSession().removeAttribute("ACCOUNT_LOCKED_REASON");
				}
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
