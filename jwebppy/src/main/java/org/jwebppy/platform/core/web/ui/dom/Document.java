package org.jwebppy.platform.core.web.ui.dom;

import java.util.LinkedList;
import java.util.List;

public class Document extends LinkedList<Object>
{
	private static final long serialVersionUID = -7558560031860783236L;

	public static Document EMPTY = new Document();

	public void addElement(Element element)
	{
		this.add(element);
	}

	public void addElements(List<?> elements)
	{
		this.addAll(elements);
	}
}
