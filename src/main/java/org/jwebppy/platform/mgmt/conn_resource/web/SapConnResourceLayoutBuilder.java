package org.jwebppy.platform.mgmt.conn_resource.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.form.Checkbox;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.form.Select;
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
		thTr.addTextTh("Name", "two wide");
		thTr.addTextTh("Description", "two wide");
		thTr.addTextTh("Type", "three wide");
		thTr.addTextTh("Application/Message Server", "two wide");//Message or Application
		thTr.addTextTh("System ID", "one wide");
		thTr.addTextTh("R3name", "one wide");
		thTr.addTextTh("Client", "one wide");
		thTr.addTextTh("Username", "two wide");
		thTr.addTextTh("Use", "one wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		List<SapConnResourceDto> sapConnResources = ListUtils.emptyIfNull(pageableList.getList());

		Tbody tbody = new Tbody();

		for (SapConnResourceDto sapConnResource : sapConnResources)
		{
			Tr tbTr = new Tr();

			if (CollectionUtils.isEmpty(sapConnResource.getUserGroups()))
			{
				tbTr.addDataKeyCheckboxTd("scrSeq", sapConnResource.getScrSeq());
			}
			else
			{
				tbTr.addEmptyTd();
			}

			tbTr.addDataKeyLinkTd(sapConnResource.getName(), sapConnResource.getScrSeq());
			tbTr.addTextTd(sapConnResource.getDescription());
			tbTr.addTextTd(sapConnResource.getType().getType());
			tbTr.addTextTd(CmStringUtils.defaultIfEmpty(sapConnResource.getMsgServer(), sapConnResource.getAppServer()));
			tbTr.addTextTd(sapConnResource.getSystemId());
			tbTr.addTextTd(sapConnResource.getR3name());
			tbTr.addTextTd(sapConnResource.getClient());
			tbTr.addTextTd(sapConnResource.getUsername());
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
		}
		else
		{
			elementMap.put("Message Server", sapConnResource.getMsgServer());
			elementMap.put("Group/Server", sapConnResource.getGrpServer());
		}

		elementMap.put("System ID", sapConnResource.getSystemId());
		elementMap.put("R3name", sapConnResource.getR3name());
		elementMap.put("Client", sapConnResource.getClient());
		elementMap.put("Username", sapConnResource.getUsername());
		elementMap.put("Pool Capacity", sapConnResource.getPoolCapacity());
		elementMap.put("Peak Limit", sapConnResource.getPeakLimit());
		elementMap.put("Language", sapConnResource.getLanguage());
		//elementMap.put("Use User Language", sapConnResource.getFgUseUserLang());
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

		document.addElement(loName);
		document.addElement(loDescription);

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

		Input loSystemId = new Input("systemId", sapConnResource.getSystemId());
		loSystemId.setLabel("System ID");

		Input loR3name = new Input("r3name", sapConnResource.getR3name());
		loR3name.setRequired(true);
		loR3name.setLabel("R3name");

		Input loClient = new Input("client", sapConnResource.getClient());
		loClient.setLabel("Client");

		Input loPoolCapacity = new Input("poolCapacity", sapConnResource.getPoolCapacity());
		loPoolCapacity.setLabel("Pool Capacity");

		Input loPeakLimit = new Input("peakLimit", sapConnResource.getPeakLimit());
		loPeakLimit.setLabel("Peak Limit");

		Input loUsername = new Input("username", sapConnResource.getUsername());
		loUsername.setLabel("Username");

		Input loPassword = new Input("password", sapConnResource.getDecodedPassword());
		loPassword.setLabel("Password");

		if (ObjectUtils.isEmpty(sapConnResource.getScrSeq()))
		{
			loUsername.setRequired(true);
			loPassword.setRequired(true);
		}

		Select loLanguage = new Select("language");
		loLanguage.setLabel("Language");
		loLanguage.setRequired(true);
		loLanguage.setValue(sapConnResource.getLanguage());
		loLanguage.addOption("en", new Locale("en").getDisplayLanguage());
		loLanguage.addOption("ko", new Locale("ko").getDisplayLanguage());

		//Element loFgUseUserLang = new Checkbox("fgUseUserLang", MgmtCommonVo.YES, sapConnResource.getFgUseUserLang());
		//loFgUseUserLang.setLabel("Use User Language");

		Element loFgUse = new Checkbox("fgUse", MgmtCommonVo.YES, sapConnResource.getFgUse());
		loFgUse.setLabel("Use");

		document.addElement(loSystemId);
		document.addElement(loR3name);
		document.addElement(loClient);
		document.addElement(loPoolCapacity);
		document.addElement(loPeakLimit);
		document.addElement(loUsername);
		document.addElement(loPassword);
		document.addElement(loLanguage);
		//document.addElement(loFgUseUserLang);
		document.addElement(loFgUse);

		return document;
	}
}
