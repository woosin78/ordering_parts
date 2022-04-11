package org.jwebppy.platform.core.web.ui.dom.form;

public class Input extends Form
{
	public Input()
	{
		super("input");
	}

	public Input(String name)
	{
		super("input");

		setType("text");
		setName(name);
	}

	public Input(String name, Object value)
	{
		super("input");

		setType("text");
		setName(name);
		setValue(value);
	}

	public Input(String type, String name, Object value)
	{
		super("input");

		setName(name);
		setType(type);
		setValue(value);
	}

	public Input(String type, String name, String value, String clazz)
	{
		super("input");

		setName(name);
		setType(type);
		setValue(value);
		setClass(clazz);
	}
}
