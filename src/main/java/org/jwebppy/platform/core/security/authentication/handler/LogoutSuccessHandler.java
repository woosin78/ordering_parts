package org.jwebppy.platform.core.security.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.security.authentication.dto.LogoutHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;
import org.jwebppy.platform.core.security.authentication.service.LogoutHistoryService;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
{
	@Autowired
	private LogoutHistoryService logoutHistoryService;

	@Autowired
	private UserService userService;

    @Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
    	if (ObjectUtils.isNotEmpty(authentication))
    	{
    		PlatformUserDetails platformUserDetails = (PlatformUserDetails)authentication.getDetails();

    		if (platformUserDetails != null)
    		{
    			UserDto user = userService.getUser(platformUserDetails.getUseq());

                LogoutHistoryDto logoutHistory = new LogoutHistoryDto();
                logoutHistory.setUseq(user.getUseq());
                logoutHistory.setSessionId(request.getSession().getId());
                logoutHistory.setReferer(request.getHeader("referer"));
                logoutHistory.setTimezone(user.getTimezone());

                logoutHistoryService.createLogoutHistory(logoutHistory);
    		}
    	}

    	request.getSession().invalidate();

        super.onLogoutSuccess(request, response, authentication);
    }
}
