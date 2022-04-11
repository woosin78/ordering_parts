package org.jwebppy.platform.core.web.ui.dom.table;

import org.jwebppy.platform.core.web.ui.dom.Element;

public class Thead extends Element
{
	public Thead()
	{
		super("THEAD");
	}

	public void addTr(Element element)
	{
		addElement(element);
	}
}
