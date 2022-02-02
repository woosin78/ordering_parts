package org.jwebppy.platform.core.web.ui.dom.form;

import org.jwebppy.platform.core.util.CmStringUtils;

public class Radio extends Checkbox
{
	public Radio()
	{
		super();

		setType("radio");
	}

	public Radio(String name, Object value)
	{
		super(name, value);

		setType("radio");
	}

	public Radio(String name, Object value, Object checkedValue)
	{
		super(name, value);

		setType("radio");

		addAttribute("data-value", CmStringUtils.trimToEmpty(checkedValue));

		if (CmStringUtils.equals(value, checkedValue))
		{
			checked();
		}
	}
}
