package org.jwebppy.platform.mgmt.conn_resource.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.form.Checkbox;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.layout.PlatformLayoutBuildUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnType;

public class SapConnResourceLayoutBuilder
{
	public static Document pageableList(PageableList<SapConnResourceDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Name", "three wide");
		thTr.addTextTh("Description", "three wide");
		thTr.addTextTh("System ID", "one wide");
		thTr.addTextTh("Group/Server", "three wide");
		thTr.addTextTh("Instance No.", "one wide");
		thTr.addTextTh("Message Server / Application Server", "three wide");
		thTr.addTextTh("Use", "one wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		List<SapConnResourceDto> sapConnResources = ListUtils.emptyIfNull(pageableList.getList());

		Tbody tbody = new Tbody();

		for (SapConnResourceDto sapConnResource : sapConnResources)
		{
			Tr tbTr = new Tr();
			tbTr.addDataKeyCheckboxTd("scrSeq", sapConnResource.getScrSeq());
			tbTr.addDataKeyLinkTd(sapConnResource.getName(), sapConnResource.getScrSeq());
			tbTr.addTextTd(sapConnResource.getDescription());
			tbTr.addTextTd(sapConnResource.getSystemId());
			tbTr.addTextTd(sapConnResource.getGrpServer());
			tbTr.addTextTd(sapConnResource.getInstanceNo());
			tbTr.addTextTd(CmStringUtils.defaultIfEmpty(sapConnResource.getMsgServer(), "-") + " / " + CmStringUtils.defaultIfEmpty(sapConnResource.getAppServer(), "-"));
			tbTr.addTextTd(sapConnResource.getFgUse());

			tbody.addTr(tbTr);
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody, pageableList));

		return document;
	}

	public static Document view(SapConnResourceDto sapConnResource)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Connection Type", sapConnResource.getType().getType());
		elementMap.put("Name", sapConnResource.getName());
		elementMap.put("Description", sapConnResource.getDescription());

		if (SapConnType.C.equals(sapConnResource.getType()))
		{
			elementMap.put("Application Server", sapConnResource.getAppServer());
			elementMap.put("Instance Number", sapConnResource.getInstanceNo());
			elementMap.put("System ID", sapConnResource.getSystemId());
		}
		else
		{
			elementMap.put("System ID", sapConnResource.getSystemId());
			elementMap.put("Message Server", sapConnResource.getMsgServer());
			elementMap.put("Group/Server", sapConnResource.getGrpServer());
		}

		elementMap.put("Client", sapConnResource.getClient());
		elementMap.put("Username", sapConnResource.getUsername());
		elementMap.put("Password", sapConnResource.getPassword());
		elementMap.put("Pool Capacity", sapConnResource.getPoolCapacity());
		elementMap.put("Peak Limit", sapConnResource.getPeakLimit());
		elementMap.put("Use", sapConnResource.getFgUse());
		elementMap.put("Reg. Username", sapConnResource.getRegUsername());
		elementMap.put("Reg. Date", sapConnResource.getDisplayRegDate());
		elementMap.put("Mod. Username", sapConnResource.getModUsername());
		elementMap.put("Mod. Date", sapConnResource.getDisplayModDate());

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document write(SapConnResourceDto sapConnResource)
	{
		Document document = new Document();

		Input loName = new Input("name", sapConnResource.getName());
		loName.setRequired(true);
		loName.setLabel("Name");

		Input loDescription = new Input("description", sapConnResource.getDescription());
		loDescription.setLabel("Description");

		Input loSystemId = new Input("systemId", sapConnResource.getSystemId());
		loSystemId.setLabel("System ID");

		document.addElement(loName);
		document.addElement(loDescription);
		document.addElement(loSystemId);

		if (SapConnType.C.equals(sapConnResource.getType()))
		{
			Input loAppServer = new Input("appServer", sapConnResource.getAppServer());
			loAppServer.setRequired(true);
			loAppServer.setLabel("Application Server");

			Input loInstanceNo = new Input("instanceNo", sapConnResource.getInstanceNo());
			loInstanceNo.setLabel("Instance Number");

			document.addElement(loAppServer);
			document.addElement(loInstanceNo);
		}
		else
		{
			Input loMsgServer = new Input("msgServer", sapConnResource.getMsgServer());
			loMsgServer.setRequired(true);
			loMsgServer.setLabel("Message Server");

			Input logGrpServer = new Input("grpServer", sapConnResource.getGrpServer());
			logGrpServer.setRequired(true);
			logGrpServer.setLabel("Group/Server");

			document.addElement(loMsgServer);
			document.addElement(logGrpServer);
		}

		Element loFgUse = new Checkbox("fgUse", PlatformCommonVo.YES, sapConnResource.getFgUse());
		loFgUse.setLabel("Use");

		document.addElement(loFgUse);

		return document;
	}
}
