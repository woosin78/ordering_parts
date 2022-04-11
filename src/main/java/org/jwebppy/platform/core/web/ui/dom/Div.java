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

	public static Element divider()
	{
		Element element = new Div();
		element.setClass("ui fitted divider");

		return element;
	}

	public static Element hiddenDivider()
	{
		Element element = new Div();
		element.setClass("ui hidden fitted divider");

		return element;
	}
}
