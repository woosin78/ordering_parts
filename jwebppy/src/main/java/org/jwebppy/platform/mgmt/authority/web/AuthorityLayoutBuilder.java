package org.jwebppy.platform.mgmt.authority.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Th;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.layout.PlatformLayoutBuildUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserContactInfoDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;

public class AuthorityLayoutBuilder
{
	public static Document getList(PageableList<CItemDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Type", "one wide");
		thTr.addTextTh("Name", "two wide");
		thTr.addTextTh("Description", "three wide");
		thTr.addTextTh("Valid From", "two wide");
		thTr.addTextTh("Valid To", "two wide");
		thTr.addTextTh("Reg.Date", "two wide");
		thTr.addTextTh("Reg.Username", "two wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<CItemDto> cItems = pageableList.getList();

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItem : cItems)
			{
				Tr tbTr = new Tr();

				if (cItem.getType().equals(CItemType.G))
				{
					tbTr.addDataKeyCheckboxTd("cSeq", cItem.getCSeq());
				}
				else
				{
					tbTr.addTextTd("");
				}

				tbTr.addTextTd(cItem.getType().getType());
				tbTr.addDataKeyLinkTd(cItem.getName(), cItem.getCSeq());
				tbTr.addTextTd(cItem.getDescription());
				tbTr.addTextTd(cItem.getDisplayFromValid());
				tbTr.addTextTd(cItem.getDisplayToValid());
				tbTr.addTextTd(cItem.getDisplayRegDate());
				tbTr.addTextTd(cItem.getRegUsername());

				tbody.addTr(tbTr);
			}
		}

		Table table = new Table(pageableList);
		table.addThead(thead);
		table.addTbody(tbody);

		Document document = new Document();
		document.add(table);

		return document;
	}

	public static Document getGeneralInfo(CItemDto cItem)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();

		elementMap.put("Name", CmStringUtils.trimToEmpty(cItem.getName()));
		elementMap.put("Description", CmStringUtils.trimToEmpty(cItem.getDescription()));
		elementMap.put("Valid From", cItem.getDisplayFromValid());
		elementMap.put("Valid To", cItem.getDisplayToValid());
		elementMap.put("Reg.Date", cItem.getDisplayRegDate());
		elementMap.put("Reg.Username", CmStringUtils.trimToEmpty(cItem.getRegUsername()));

		if (cItem.getCSeq() != null)
		{
			elementMap.put("Mod.Date", cItem.getDisplayModDate());
			elementMap.put("Mod.Username", cItem.getModUsername());
		}

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document getGeneralInfoForm(CItemDto cItem)
	{
		Element loName = new Input("name", cItem.getName());
		loName.setLabel("Name");
		loName.addAttribute("REQUIRED");
		loName.addAttribute("style", "text-transform: uppercase");

		Element loDescription = new Input("description", cItem.getDescription());
		loDescription.setLabel("Description");

		Input loFromValid = new Input("date", "fromValid", CmStringUtils.defaultString(cItem.getDisplayFromValid(), CmDateFormatUtils.now()));
		loFromValid.setId("fromValid");
		loFromValid.setLabel("Valid From");

		Input loToValid = new Input("date", "toValid", CmStringUtils.defaultString(cItem.getDisplayToValid(), CmDateFormatUtils.unlimitDate()));
		loToValid.setId("toValid");
		loToValid.setLabel("Valid To");

		Document document = new Document();
		document.addElement(loName);
		document.addElement(loDescription);
		document.addElement(loFromValid);
		document.addElement(loToValid);

		return document;
	}

	public static Document getUsers(List<UserDto> users)
	{
		Tr thTr = new Tr();
		thTr.addTextTh("Username", "two wide");
		thTr.addTextTh("Name", "three wide");
		thTr.addTextTh("E-mail", "four wide");
		thTr.addTextTh("Company", "three wide");
		thTr.addTextTh("Department", "three wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		if (CollectionUtils.isNotEmpty(users))
		{
			for (UserDto user : users)
			{
				UserAccountDto userAccount = user.getUserAccount();
				UserContactInfoDto userContactInfo = user.getUserContactInfo();

				Tr tbTr = new Tr();
				tbTr.addTextTd(userAccount.getUsername());
				tbTr.addTextTd(user.getName());
				tbTr.addTextTd(userContactInfo.getEmail());
				tbTr.addTextTd(user.getCompany());
				tbTr.addTextTd(user.getDepartment());

				tbody.addTr(tbTr);
			}
		}

		Table table = new Table();
		table.addThead(thead);
		table.addTbody(tbody);

		Document document = new Document();
		document.add(table);

		return document;
	}

	public static Document getAuthority(List<CItemDto> cItems)
	{
		Document document = new Document();

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItemDto : cItems)
			{
				document.addElement(PlatformLayoutBuildUtils.defaultLabelText(cItemDto.getName(), cItemDto.getDescription()));
			}
		}

		return document;
	}

	public static Document getAuthorityForm(List<CItemDto> cItems)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTh(new Th("Name"));
		thTr.addTh(new Th("Description"));

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItemDto : cItems)
			{
				Tr tbTr = new Tr();
				tbTr.addDataKeyCheckboxTd("cSeq", cItemDto.getCSeq());
				tbTr.addTextTd(cItemDto.getName());
				tbTr.addTextTd(cItemDto.getDescription());

				tbody.addTr(tbTr);
			}
		}

		Table table = new Table();
		table.addThead(thead);
		table.addTbody(tbody);

		Document document = new Document();
		document.addElement(table);

		return document;
	}
}
