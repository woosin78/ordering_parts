package org.jwebppy.platform.mgmt.user.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Link;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.form.InputHidden;
import org.jwebppy.platform.core.web.ui.dom.form.Select;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;

import com.ibm.icu.util.TimeZone;

public class UserGroupLayoutBuilder
{
	public static Document pageableList(PageableList<UserGroupDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Name", "three wide");
		thTr.addTextTh("Description", "three wide");
		thTr.addTextTh("Date Format<br/>(Back-End / Front-End)", "two wide");
		thTr.addTextTh("Time Format<br/>(Back-End / Front-End)", "two wide");
		thTr.addTextTh("Country", "two wide");
		thTr.addTextTh("Timezone", "two wide");
		thTr.addTextTh("Users", "one wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		for (UserGroupDto userGroup : ListUtils.emptyIfNull(pageableList.getList()))
		{
			int userCount = userGroup.getUserCount();

			Tr tbTr = new Tr();

			if (userCount == 0)
			{
				tbTr.addDataKeyCheckboxTd("ugSeq", userGroup.getUgSeq());
			}
			else
			{
				tbTr.addEmptyTd();
			}

			tbTr.addDataKeyLinkTd(userGroup.getName(), userGroup.getUgSeq());
			tbTr.addTextTd(userGroup.getDescription());
			tbTr.addTextTd(userGroup.getDateFormat1() + " / " + userGroup.getDateFormat2());
			tbTr.addTextTd(userGroup.getTimeFormat2() + " / " + userGroup.getTimeFormat2());
			tbTr.addTextTd(userGroup.getDisplayCountry());
			tbTr.addTextTd(userGroup.getDisplayTimezone());
			tbTr.addTextTd(userCount);

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
		elementMap.put("Date Format (Back-End)", userGroup.getDateFormat1());
		elementMap.put("Time Format (Back-End)", userGroup.getTimeFormat1());
		elementMap.put("Date Format (Front-End)", userGroup.getDateFormat2());
		elementMap.put("Time Format (Front-End)", userGroup.getTimeFormat2());
		elementMap.put("Country", userGroup.getDisplayCountry());
		elementMap.put("Timezone", userGroup.getDisplayTimezone());
		elementMap.put("Default SAP Connection Resource", loSapConnResource);
		elementMap.put("Reg. Username", userGroup.getRegUsername());
		elementMap.put("Reg. Date", userGroup.getDisplayRegDate());
		elementMap.put("Mod. Username", userGroup.getModUsername());
		elementMap.put("Mod. Date", userGroup.getDisplayModDate());
		elementMap.put("userCount", new InputHidden("userCount", userGroup.getUserCount()));

		Document document = new Document();
		//document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));



		document.addDefaultLabelText("Name", userGroup.getName());
		document.addDefaultLabelText("Description", userGroup.getDescription());
		document.addDefaultLabelText("Date Format (Back-End)", userGroup.getDateFormat1());
		document.addDefaultLabelText("Time Format (Back-End)", userGroup.getTimeFormat1());
		document.addDefaultLabelText("Date Format (Front-End)", userGroup.getDateFormat2());
		document.addDefaultLabelText("Time Format (Front-End)", userGroup.getTimeFormat2());
		document.addDefaultLabelText("Country", userGroup.getDisplayCountry());
		document.addDefaultLabelText("Timezone", userGroup.getDisplayTimezone());
		document.addDefaultLabelText("Default SAP Connection Resource", loSapConnResource);
		document.addDefaultLabelText("Users", userGroup.getUserCount());
		document.addDefaultLabelText("Reg. Username", userGroup.getRegUsername());
		document.addDefaultLabelText("Reg. Date", userGroup.getDisplayRegDate());
		document.addDefaultLabelText("Mod. Username", userGroup.getModUsername());
		document.addDefaultLabelText("Mod. Date", userGroup.getDisplayModDate());
		document.addElement(new InputHidden("userCount", userGroup.getUserCount()));

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

		Input loDateFormat1 = new Input("dateFormat1", userGroup.getDateFormat1());
		loDateFormat1.setLabel("Date Format(Back-End)");
		loDateFormat1.setRequired(true);

		Input loTimeFormat1 = new Input("timeFormat1", userGroup.getTimeFormat1());
		loTimeFormat1.setLabel("Time Format(Back-End)");
		loTimeFormat1.setRequired(true);

		Input loDateFormat2 = new Input("dateFormat2", userGroup.getDateFormat2());
		loDateFormat2.setLabel("Date Format(Front-End)");
		loDateFormat2.setRequired(true);

		Input loTimeFormat2 = new Input("timeFormat2", userGroup.getTimeFormat2());
		loTimeFormat2.setLabel("Time Format(Front-End)");
		loTimeFormat2.setRequired(true);

		Select loCountry = new Select("country");
		loCountry.setLabel("Country");
		loCountry.setRequired(true);
		loCountry.setValue(CmStringUtils.defaultIfEmpty(userGroup.getCountry(), PlatformCommonVo.DEFAULT_COUNTRY));

		String[] locales = Locale.getISOCountries();

		for (String loCountryCode : locales)
		{
			Locale locale = new Locale("", loCountryCode);

			loCountry.addOption(locale.getCountry(), locale.getDisplayCountry());
		}

		Select loTimezone = new Select("timezone");
		loTimezone.setLabel("Timezone");
		loTimezone.setRequired(true);
		loTimezone.setValue(CmStringUtils.defaultIfEmpty(userGroup.getTimezone(), PlatformCommonVo.DEFAULT_TIMEZONE));

		String[] ids = TimeZone.getAvailableIDs(userGroup.getCountry());

		for (String id : ids)
		{
			loTimezone.addOption(id, id + ", " + TimeZone.getTimeZone(id).getDisplayName());
		}

		Document document = new Document();
		document.addElement(loUgSeq);
		document.addElement(loName);
		document.addElement(loDescription);
		document.addElement(loDateFormat1);
		document.addElement(loTimeFormat1);
		document.addElement(loDateFormat2);
		document.addElement(loTimeFormat2);
		document.addElement(loCountry);
		document.addElement(loTimezone);
		document.addElement(loSapConnResource);

		return document;
	}
}
