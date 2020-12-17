package org.jwebppy.platform.core.web.ui.dom.form;

public class Textarea extends Form
{
	public Textarea()
	{
		super("textarea");
	}

	public Textarea(String name)
	{
		super("textarea");

		setName(name);
	}
}
