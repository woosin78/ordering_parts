package org.jwebppy.platform.core.web.ui.dom.form;

public class InputHidden extends Input
{
	public InputHidden()
	{
		super();
		setType("hidden");
	}

	public InputHidden(String name)
	{
		super("hidden", name);
	}

	public InputHidden(String name, Object value)
	{
		super("hidden", name, value);
	}
}
