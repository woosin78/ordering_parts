package org.jwebppy.platform.mgmt.i18n.dto;

public enum LangKindType
{
	/*
	Chinese: zh_CN
	English: en
	French: fr
	German: de
	Italian: it
	Spanish: es
	Korean: ko
	 */

	en("English"), ko("Korean"), fr("French"), de("German"), it("Italian"), es("Spanish"), zh_CN("Chinese");

	private String type;

	private LangKindType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}
}
