package org.jwebppy.platform.core.web.ui.dom;

import org.jwebppy.platform.core.util.CmStringUtils;

public class Div extends Element
{
	public Div()
	{
		super("DIV");
	}

	public Div(Object text)
	{
		super("DIV", CmStringUtils.trimToEmpty(text));
	}
}
