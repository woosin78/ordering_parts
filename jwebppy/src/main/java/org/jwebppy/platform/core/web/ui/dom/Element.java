package org.jwebppy.platform.core.web.ui.dom;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Element
{
	private String tagName;
	private String text;
	private Label label;
	private final List<Attribute> attributes =  new LinkedList<>();
	private final List<Element> children =  new LinkedList<>();

	public Element() {}

	public Element(String tagName)
	{
		setTagName(tagName);
	}

	public Element(String tagName, Object text)
	{
		setTagName(tagName);
		this.text = CmStringUtils.trimToEmpty(text);
	}

	public void setTagName(String tagName)
	{
		this.tagName = CmStringUtils.lowerCase(tagName);
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setText(Object text)
	{
		this.text = CmStringUtils.trimToEmpty(text);
	}

	public String getText()
	{
		return text;
	}

	public void setLabel(String text)
	{
		this.label = new Label(text);
	}

	public void setLabel(Label label)
	{
		this.label = label;
	}

	public Label getLabel()
	{
		return label;
	}

	public void setClass(String clazz)
	{
		addAttribute("class", clazz);
	}

	public void addAttribute(Attribute attribute)
	{
		if (attribute != null && attribute.isNotEmpty())
		{
			Attribute oAttribute = findAttribute(attribute.getName());

			if (oAttribute != null)
			{
				attributes.remove(oAttribute);
			}

			attributes.add(attribute);
		}
	}

	public void addAttribute(String name)
	{
		addAttribute(new Attribute(name));
	}

	public void addAttribute(String name, Object value)
	{
		addAttribute(new Attribute(name, value));
	}

	public void addElement(Element element)
	{
		if (element != null)
		{
			children.add(element);
		}
	}

	public Attribute findAttribute(String name)
	{
		if (CollectionUtils.isNotEmpty(attributes))
		{
			for (Attribute attribute : attributes)
			{
				if (CmStringUtils.equalsIgnoreCase(attribute.getName(), name))
				{
					return attribute;
				}
			}
		}

		return null;
	}

	public void setRequired(boolean isRequired)
	{
		if (isRequired)
		{
			addAttribute("required");
		}
	}
}
