package org.jwebppy.platform.core.web.ui.dom;

import org.jwebppy.platform.core.util.CmStringUtils;

public class Link extends Element
{
	public Link()
	{
		super("A");

		addAttribute(new Attribute("style", "cursor:pointer;"));
	}

	public Link(String text)
	{
		super("A", text);

		addAttribute(new Attribute("style", "cursor:pointer;"));
	}

	public void href(String link, String target)
	{
		addAttribute(new Attribute("href", CmStringUtils.defaultIfEmpty(link, "#")));

		if (CmStringUtils.isNotEmpty(target))
		{
			addAttribute(new Attribute("target", target));
		}
	}

	public void setKey(Object key)
	{
		addAttribute(new Attribute("data-key", key));
	}
}
