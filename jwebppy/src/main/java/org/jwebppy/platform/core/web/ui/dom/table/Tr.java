package org.jwebppy.platform.core.web.ui.dom.table;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.form.Checkbox;

public class Tr extends Element
{
	public Tr()
	{
		super("TR");
	}

	public void addTextTh(Object text)
	{
		addElement(new Th(text));
	}

	public void addTextTh(Object text, String clazz)
	{
		Th th = new Th(text);
		th.addAttribute("CLASS", clazz);

		addElement(th);
	}

	public void addTh(Element element)
	{
		addElement(element);
	}

	public void addCheckAllTh()
	{
		Element checkAll = new Checkbox();
		checkAll.addAttribute("CLASS", "master");

		Th th = new Th();
		th.addAttribute("CLASS", "one wide no-sort");
		th.addElement(checkAll);

		addElement(th);
	}

	public void addTextTd(Object text)
	{
		addElement(new Td(text));
	}

	public void addEmptyTd()
	{
		addElement(new Td(""));
	}

	public void addTd(Element element)
	{
		Element td = new Td();
		td.addElement(element);

		addElement(td);
	}

	public void addDataKeyLinkTd(Object text, Object key)
	{
		Element link = new Element("a", text);
		link.addAttribute("href", "#");
		link.addAttribute("data-key", key);

		Element td = new Td();
		td.addElement(link);

		addElement(td);
	}

	public void addDataKeyCheckboxTd(String name, Object key)
	{
		Element checkbox = new Checkbox(name, key);
		checkbox.addAttribute("data-key", key);

		Element td = new Td();
		td.addElement(checkbox);

		addElement(td);
	}

	public void addCheckboxTd(String name, Object key, String value, String defaultValue)
	{
		Element checkbox = new Checkbox(name, value);

		if (CmStringUtils.equals(value, defaultValue))
		{
			checkbox.addAttribute("checked");
		}

		Element td = new Td();
		td.addElement(checkbox);

		addElement(td);
	}
}
