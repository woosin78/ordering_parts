package org.jwebppy.platform.mgmt.authority.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.form.Checkbox;
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
	public static Document pageableList(PageableList<CItemDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Type", "one wide");
		thTr.addTextTh("Name", "two wide");
		thTr.addTextTh("Description", "four wide");
		thTr.addTextTh("Valid From", "two wide");
		thTr.addTextTh("Valid To", "two wide");
		thTr.addTextTh("Reg.Date", "two wide");
		thTr.addTextTh("Reg.Username", "two wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<CItemDto> cItems = ListUtils.emptyIfNull(pageableList.getList());

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

		Document document = new Document();
		document.addElement(new Table(thead, tbody, pageableList));

		return document;
	}

	public static Document viewGeneralInfo(CItemDto cItem)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();

		elementMap.put("Name", cItem.getName());
		elementMap.put("Description", cItem.getDescription());
		elementMap.put("Valid From", cItem.getDisplayFromValid());
		elementMap.put("Valid To", cItem.getDisplayToValid());
		elementMap.put("Visible", cItem.getFgVisible());
		elementMap.put("Reg.Date", cItem.getDisplayRegDate());
		elementMap.put("Reg.Username", cItem.getRegUsername());

		if (cItem.getCSeq() != null)
		{
			elementMap.put("Mod.Date", cItem.getDisplayModDate());
			elementMap.put("Mod.Username", cItem.getModUsername());
		}

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document writeGeneralInfo(CItemDto cItem)
	{
		Input loName = new Input("name", cItem.getName());
		loName.setLabel("Name");
		loName.setStyle("text-transform: uppercase");
		loName.setRequired(true);

		Input loDescription = new Input("description", cItem.getDescription());
		loDescription.setLabel("Description");

		Input loFromValid = new Input("date", "fromValid", CmStringUtils.defaultString(cItem.getDisplayFromValid(), CmDateFormatUtils.now()));
		loFromValid.setId("fromValid");
		loFromValid.setLabel("Valid From");

		Input loToValid = new Input("date", "toValid", CmStringUtils.defaultString(cItem.getDisplayToValid(), CmDateFormatUtils.unlimitDate()));
		loToValid.setId("toValid");
		loToValid.setLabel("Valid To");

		Checkbox loFgVisible = new Checkbox("fgVisible", PlatformCommonVo.YES, cItem.getFgVisible());
		loFgVisible.setLabel("Visible");

		Document document = new Document();
		document.addElement(loName);
		document.addElement(loDescription);
		document.addElement(loFromValid);
		document.addElement(loToValid);
		document.addElement(loFgVisible);

		return document;
	}

	public static Document listUser(List<UserDto> users)
	{
		Tr thTr = new Tr();
		thTr.addTextTh("Status", "one wide");
		thTr.addTextTh("Username", "two wide");
		thTr.addTextTh("Name", "three wide");
		thTr.addTextTh("E-mail", "four wide");
		thTr.addTextTh("Company", "four wide");
		thTr.addTextTh("User Group", "two wide");

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

				String status = "<i class='lock icon'></i>";

				if (CmStringUtils.equals(userAccount.getFgAccountLocked(), PlatformCommonVo.NO))
				{
					status = "<i class='lock open icon'></i>";
				}

				tbTr.addTextTd(status);
				tbTr.addDataKeyLinkTd(userAccount.getUsername(), user.getUSeq(), "uSeq");
				tbTr.addTextTd(user.getName());
				tbTr.addTextTd(userContactInfo.getEmail());
				tbTr.addTextTd(user.getCompany());
				tbTr.addDataKeyLinkTd(user.getUserGroup().getName(), user.getUserGroup().getUgSeq(), "ugSeq");

				tbody.addTr(tbTr);
			}
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody));

		return document;
	}

	public static Document viewAuthority(List<CItemDto> cItems)
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

	public static Document writeAuthority(List<CItemDto> cItems)
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

		Document document = new Document();
		document.addElement(new Table(thead, tbody));

		return document;
	}
}
