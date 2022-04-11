package org.jwebppy.platform.core.web.ui.dom;

import org.jwebppy.platform.core.util.CmStringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attribute
{
	private String name;
	private String value;

	public Attribute() {}

	public Attribute(String name)
	{
		this.name = CmStringUtils.trimToEmpty(name);
	}

	public Attribute(String name, Object value)
	{
		this.name = CmStringUtils.upperCase(name);
		this.value = CmStringUtils.trimToEmpty(value);
	}

	public boolean isEmpty()
	{
		if (CmStringUtils.isEmpty(name))
		{
			return true;
		}

		return false;
	}

	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
}
