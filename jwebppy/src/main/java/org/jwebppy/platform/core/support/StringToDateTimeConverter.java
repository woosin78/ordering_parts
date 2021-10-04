package org.jwebppy.platform.core.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDateTimeConverter implements Converter<String, LocalDateTime>
{
	@Override
	public LocalDateTime convert(String source)
	{
		if (CmStringUtils.isNotEmpty(source))
		{
			return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(UserAuthenticationUtils.getUserDetails().getDateTimeFormat1()));
		}

		return null;
	}
}
