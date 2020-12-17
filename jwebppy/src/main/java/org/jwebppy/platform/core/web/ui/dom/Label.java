package org.jwebppy.platform.core.web.ui.dom;

import org.jwebppy.platform.core.util.CmStringUtils;

public class Label extends Element
{
	public Label(String text)
	{
		super("LABEL", CmStringUtils.trimToEmpty(text));
	}
}
