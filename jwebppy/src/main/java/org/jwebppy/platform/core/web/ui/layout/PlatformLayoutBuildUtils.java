package org.jwebppy.platform.core.web.ui.layout;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Element;

public class PlatformLayoutBuildUtils
{
	public static Element defaultLabelText(String label, String text)
	{
		Element textElement = new Element("div", text);
		textElement.addAttribute("class", "sub header");

		Element labelElement = new Element("div", label);
		labelElement.addAttribute("class", "ui dividing header small");
		labelElement.addElement(textElement);

		return labelElement;
	}

	public static List<Element> simpleLabelTexts(Map<String, Object> elementMap)
	{
		if (MapUtils.isNotEmpty(elementMap))
		{
			List<Element> elements = new LinkedList<>();

			Iterator<Entry<String, Object>> it = elementMap.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<String, Object> entry = it.next();

				elements.add(defaultLabelText(entry.getKey(), CmStringUtils.trimToEmpty(entry.getValue())));
			}

			return elements;
		}

		return Collections.emptyList();
	}
}
