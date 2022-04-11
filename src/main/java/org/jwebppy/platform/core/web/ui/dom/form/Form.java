package org.jwebppy.platform.core.web.ui.dom.form;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Element;

public class Form extends Element
{
	public Form() {}

	public Form(String tagName)
	{
		super(tagName);
	}

	public void setId(String id)
	{
		addAttribute("id", id);
	}

	public void setName(String name)
	{
		addAttribute("name", name);
	}

	public void setType(String type)
	{
		addAttribute("type", CmStringUtils.upperCase(type));
	}

	public void setValue(Object value)
	{
		this.addAttribute("value", CmStringUtils.trimToEmpty(value));
	}
}
