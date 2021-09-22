package org.jwebppy.platform.mgmt.user.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
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
		thTr.addTextTh("Name", "four wide");
		thTr.addTextTh("Description", "four wide");
		thTr.addTextTh("Default SAP Connection Resource", "three wide");
		thTr.addTextTh("Reg.Username", "two wide");
		thTr.addTextTh("Reg.Date", "two wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		for (UserGroupDto userGroup : ListUtils.emptyIfNull(pageableList.getList()))
		{
			Tr tbTr = new Tr();
			tbTr.addDataKeyCheckboxTd("ugSeq", userGroup.getUgSeq());
			tbTr.addDataKeyLinkTd(userGroup.getName(), userGroup.getUgSeq());
			tbTr.addTextTd(userGroup.getDescription());
			tbTr.addTextTd(userGroup.getSapConnResource().getName());
			tbTr.addTextTd(userGroup.getRegUsername());
			tbTr.addTextTd(userGroup.getDisplayRegDate());

			tbody.addTr(tbTr);
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody, pageableList));

		return document;
	}

	public static Document view(UserGroupDto userGroup)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Name", userGroup.getName());
		elementMap.put("Description", userGroup.getDescription());
		elementMap.put("Default SAP Connection Resource", userGroup.getSapConnResource().getName());
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

		Document document = new Document();
		document.addElement(loUgSeq);
		document.addElement(loName);
		document.addElement(loDescription);
		document.addElement(loSapConnResource);

		return document;
	}
}
