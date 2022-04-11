package org.jwebppy.platform.core.web.ui.dom.form;

public class InputPassword extends Input
{
	public InputPassword()
	{
		super("input");
		setType("password");
	}

	public InputPassword(String name)
	{
		super("input");

		setType("password");
		setName(name);
	}

	public InputPassword(String name, Object value)
	{
		super("input");

		setType("password");
		setName(name);
		setValue(value);
	}
}
