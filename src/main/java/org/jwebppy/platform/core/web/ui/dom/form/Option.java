package org.jwebppy.platform.core.web.ui.dom.form;

public class Option extends Form
{
	public Option()
	{
		super("option");
	}

	public Option(Object value, Object text)
	{
		super("option");

		setValue(value);
		setText(text);
	}
}
