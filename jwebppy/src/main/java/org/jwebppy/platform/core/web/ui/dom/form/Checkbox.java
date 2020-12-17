package org.jwebppy.platform.core.web.ui.dom.form;

import org.jwebppy.platform.core.util.CmStringUtils;

public class Checkbox extends Input
{
	public Checkbox()
	{
		super();

		setType("checkbox");
	}

	public Checkbox(String name, Object value)
	{
		super(name, value);

		setType("checkbox");
	}

	public Checkbox(String name, Object value, Object checkedValue)
	{
		super(name, value);

		setType("checkbox");

		addAttribute("data-value", CmStringUtils.trimToEmpty(checkedValue));

		if (CmStringUtils.equals(value, checkedValue))
		{
			addAttribute("checked", "checked");
		}
	}
}
