package org.jwebppy.platform.core.web.ui.dom.table;

import org.jwebppy.platform.core.web.ui.dom.Element;

public class Tbody extends Element
{
	public Tbody()
	{
		super("TBODY");
	}

	public void addTr(Element element)
	{
		addElement(element);
	}
}
