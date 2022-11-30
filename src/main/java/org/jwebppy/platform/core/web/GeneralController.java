package org.jwebppy.platform.core.web;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

public abstract class GeneralController
{
	@Value("${platform.service}")
	private String PLATFORM_SERVICE;

	public final static String DEFAULT_VIEW_URL = "DUMMY";
	public final static List<?> EMPTY_RETURN_VALUE = Collections.EMPTY_LIST;

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder)
	{
		//RequestParam 을 Array 로 받을 때 구분자를 ',' 에서 '^' 으로 변경함
		webDataBinder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(PlatformConfigVo.DELIMITER));

		//문자열로 넘어온 날짜를 LocalDateTime type 으로 convert 함
		webDataBinder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException
			{
				if (CmStringUtils.isNotEmpty(text))
				{
					Object value = null;

					try
					{
						value = LocalDate.parse(text, DateTimeFormatter.ofPattern(UserAuthenticationUtils.getUserDetails().getDateFormat1()));
					}
					catch (DateTimeParseException e1)
					{
						try
						{
							value = LocalDateTime.parse(text, DateTimeFormatter.ofPattern(UserAuthenticationUtils.getUserDetails().getDateTimeFormat1()));
						}
						catch (DateTimeParseException e2)
						{
							try
							{
								value = LocalTime.parse(text, DateTimeFormatter.ofPattern(UserAuthenticationUtils.getUserDetails().getTimeFormat1()));
							}
							catch (DateTimeParseException e3) {}
						}
					}

					setValue(value);
				}
			}
		});

		//문자열의 경우 앞뒤 공백 삭제하고 공백만 있을 경우 null 을 반환
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	protected Integer getUSeq()
	{
		return UserAuthenticationUtils.getUserDetails().getUSeq();
	}

	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}

	protected boolean isProduction()
	{
		if (CmStringUtils.equals(PLATFORM_SERVICE, "PRD"))
		{
			return true;
		}

		return false;
	}

    protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
    {
        Iterator<?> it = webRequest.getParameterNames();
        String name = null;
        String[] value = null;
        int length = 0;

        while (it.hasNext())
        {
            name = (String)it.next();
            value = webRequest.getParameterValues(name);

            if (value != null)
            {
                length = value.length;

                if (length == 1)
                {
                    model.addAttribute(name, value[0]);
                }
                else
                {
                    model.addAttribute(name, value);
                }
            }
        }
    }
}