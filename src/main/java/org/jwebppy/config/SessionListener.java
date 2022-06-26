package org.jwebppy.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Value;

public class SessionListener implements HttpSessionListener
{
	@Value("${server.servlet.session.timeout}")
	private int SESSION_TIMEOUT;

	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent)
	{
		httpSessionEvent.getSession().setMaxInactiveInterval(SESSION_TIMEOUT);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se)
	{
	}
}
