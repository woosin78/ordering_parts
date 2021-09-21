package org.jwebppy.platform.core.web;

import java.util.Iterator;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

public abstract class GeneralController
{
	@Autowired
	private Environment environment;

	public final static String DEFAULT_VIEW_URL = "DUMMY";
	public final static String EMPTY_RETURN_VALUE = "";

	protected Integer getUSeq()
	{
		return UserAuthenticationUtils.getUserDetails().getUSeq();
	}

	protected String getUsername()
	{
		return UserAuthenticationUtils.getUsername();
	}

	protected String getPlatformName()
	{
		return environment.getProperty("platform.service");
	}

	protected boolean isProduction()
	{
		if (CmStringUtils.equals(getPlatformName(), "PRD"))
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

	protected void setDefaultAttribute(Model model, WebRequest webRequest)
	{
		model.addAttribute("pageNumber", CmStringUtils.defaultString(webRequest.getParameter("pageNumber"), "1"));
		model.addAttribute("rowPerPage", CmStringUtils.defaultString(webRequest.getParameter("rowPerPage"), PlatformCommonVo.DEFAULT_ROW_PER_PAGE));

		addAllAttributeFromRequest(model, webRequest);
	}

    /*
     * RequestParam 을 Array 로 받을 때 구분자를 ',' 에서 '^' 으로 변경함
     */
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder)
	{
		webDataBinder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(PlatformCommonVo.DELIMITER));
	}
}