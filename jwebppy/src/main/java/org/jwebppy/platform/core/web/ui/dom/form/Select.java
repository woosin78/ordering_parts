package org.jwebppy.platform.core.web.ui.dom.form;

import org.jwebppy.platform.core.util.CmStringUtils;

public class Select extends Form
{
	public Select()
	{
		super("select");
		setClass("ui fluid search clearable dropdown");
	}

	public Select(String name)
	{
		super("select");
		setClass("ui fluid search clearable dropdown " + name);
		setName(name);
	}

	public void addOption(Object value, Object text)
	{
		Option option = new Option(value, text);

		if (CmStringUtils.equals(findAttribute("value").getValue(), value))
		{
			option.addAttribute("selected");
		}

		addElement(option);
	}
}
