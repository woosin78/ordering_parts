package org.jwebppy.platform.security.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.platform.security.authentication.dto.LogoutHistoryDto;
import org.jwebppy.platform.security.authentication.service.LogoutHistoryService;
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
    	if (authentication != null)
    	{
            UserDto user = userService.getUserByUsername(authentication.getName());

            LogoutHistoryDto logoutHistory = new LogoutHistoryDto();
            logoutHistory.setUSeq(user.getUSeq());
            logoutHistory.setSessionId(request.getSession().getId());
            logoutHistory.setReferer(request.getHeader("referer"));

            logoutHistoryService.createLogoutHistory(logoutHistory);
    	}

        super.onLogoutSuccess(request, response, authentication);
    }
}
