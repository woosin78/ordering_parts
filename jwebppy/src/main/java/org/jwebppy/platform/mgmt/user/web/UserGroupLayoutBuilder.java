package org.jwebppy.platform.mgmt.user.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Link;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.form.InputHidden;
import org.jwebppy.platform.core.web.ui.dom.form.Select;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.layout.PlatformLayoutBuildUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;

public class UserGroupLayoutBuilder
{
	public static Document pageableList(PageableList<UserGroupDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Name", "three wide");
		thTr.addTextTh("Description", "three wide");
		thTr.addTextTh("Date Format", "two wide");
		thTr.addTextTh("Time Format", "two wide");
		thTr.addTextTh("Timezone", "two wide");
		thTr.addTextTh("Default SAP Connection Resource", "three wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		for (UserGroupDto userGroup : ListUtils.emptyIfNull(pageableList.getList()))
		{
			SapConnResourceDto sapConnResource = userGroup.getSapConnResource();

			Tr tbTr = new Tr();
			tbTr.addDataKeyCheckboxTd("ugSeq", userGroup.getUgSeq());
			tbTr.addDataKeyLinkTd(userGroup.getName(), userGroup.getUgSeq());
			tbTr.addTextTd(userGroup.getDescription());
			tbTr.addTextTd(userGroup.getDateFormat());
			tbTr.addTextTd(userGroup.getTimeFormat());
			tbTr.addTextTd(userGroup.getTimezone());
			tbTr.addDataKeyLinkTd(sapConnResource.getName(), sapConnResource.getScrSeq());

			tbody.addTr(tbTr);
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody, pageableList));

		return document;
	}

	public static Document view(UserGroupDto userGroup)
	{
		SapConnResourceDto sapConnResource = userGroup.getSapConnResource();

		Link loSapConnResource = new Link(sapConnResource.getName());
		loSapConnResource.setClass("sap-conn-resource");
		loSapConnResource.setKey(sapConnResource.getScrSeq());

		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Name", userGroup.getName());
		elementMap.put("Description", userGroup.getDescription());
		elementMap.put("Date Format", userGroup.getDateFormat());
		elementMap.put("Time Format", userGroup.getTimeFormat());
		elementMap.put("Timezone", userGroup.getTimezone());
		elementMap.put("Default SAP Connection Resource", loSapConnResource);
		elementMap.put("Reg. Username", userGroup.getRegUsername());
		elementMap.put("Reg. Date", userGroup.getDisplayRegDate());
		elementMap.put("Mod. Username", userGroup.getModUsername());
		elementMap.put("Mod. Date", userGroup.getDisplayModDate());

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document write(UserGroupDto userGroup, List<SapConnResourceDto> sapConnResources)
	{
		InputHidden loUgSeq = new InputHidden("ugSeq", userGroup.getUgSeq());
		loUgSeq.setId("ugSeq");

		Input loName = new Input("name", userGroup.getName());
		loName.setRequired(true);
		loName.setLabel("Name");

		Input loDescription = new Input("description", userGroup.getDescription());
		loDescription.setLabel("Description");

		Select loSapConnResource = new Select("scrSeq");
		loSapConnResource.setLabel("Default SAP Connection Resource");
		loSapConnResource.setValue(userGroup.getSapConnResource().getScrSeq());

		for (SapConnResourceDto sapConnResource: ListUtils.emptyIfNull(sapConnResources))
		{
			loSapConnResource.addOption(sapConnResource.getScrSeq(), sapConnResource.getName());
		}

		Input loDateFormat = new Input("dateFormat", userGroup.getDateFormat());
		loDateFormat.setLabel("Date Format");

		Input loTimeFormat = new Input("timeFormat", userGroup.getTimeFormat());
		loTimeFormat.setLabel("Time Format");

		Input loTimezone = new Input("timezone", userGroup.getTimezone());
		loTimezone.setLabel("Timezone");

		Document document = new Document();
		document.addElement(loUgSeq);
		document.addElement(loName);
		document.addElement(loDescription);
		document.addElement(loDateFormat);
		document.addElement(loTimeFormat);
		document.addElement(loTimezone);
		document.addElement(loSapConnResource);

		return document;
	}
}
