package org.jwebppy.platform.core.web.ui.dom.form;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Attribute;

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

		Attribute selectedValue = findAttribute("value");

		if (selectedValue != null)
		{
			if (CmStringUtils.equals(selectedValue.getValue(), value))
			{
				option.addAttribute("selected");
			}
		}

		addElement(option);
	}
}
