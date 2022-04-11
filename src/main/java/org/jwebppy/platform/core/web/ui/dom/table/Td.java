package org.jwebppy.platform.core.web.ui.dom.table;

import org.jwebppy.platform.core.web.ui.dom.Element;

public class Td extends Element
{
	public Td()
	{
		super("TD");
	}

	public Td(Object text)
	{
		super("TD");

		setText(text);
	}
}
