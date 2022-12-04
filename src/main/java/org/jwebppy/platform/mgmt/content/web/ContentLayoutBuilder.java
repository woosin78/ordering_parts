package org.jwebppy.platform.mgmt.content.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.form.Checkbox;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.form.Select;
import org.jwebppy.platform.core.web.ui.dom.form.Textarea;
import org.jwebppy.platform.core.web.ui.layout.PlatformLayoutBuildUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemComponentDto;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;

public class ContentLayoutBuilder
{
	public static Document viewGeneralInfo(CItemDto citem)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Item Type", citem.getType().getType());
		elementMap.put("Name", citem.getName());
		elementMap.put("Description", citem.getDescription());
		elementMap.put("Component", citem.getComponent());
		elementMap.put("Entry Point", citem.getEntryPoint());
		elementMap.put("Parameter", citem.getParameter());
		elementMap.put("Valid From", citem.getDisplayFromValid());
		elementMap.put("Valid To", citem.getDisplayToValid());
		elementMap.put("Sort", citem.getSort());
		elementMap.put("Visible", citem.getFgVisible());
		elementMap.put("Reg. Date", citem.getDisplayRegDate());
		elementMap.put("Reg. Username", citem.getRegUsername());
		elementMap.put("Mod. Date", citem.getDisplayModDate());
		elementMap.put("Mod. Username", citem.getModUsername());

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document writeGeneralInfo(CItemDto citem, CItemDto parentCItem, List<CItemComponentDto> citemComponents, List<CItemComponentDto> citemEntryPoints)
	{
		Select loType = new Select("type");
		loType.setLabel("Item Type");
		loType.setRequired(true);

		if (citem.getCseq() != null)
		{
			loType.addOption(citem.getType(), citem.getType().getType());
		}
		else
		{
			for (CItemType citemType: CItemType.availableTypes(parentCItem.getType()))
			{
				loType.addOption(citemType.name(), citemType.getType());
			}
		}

		Input loId = new Input("name", citem.getName());
		loId.setLabel("Name");
		loId.setStyle("text-transform: uppercase");
		loId.setRequired(true);

		Textarea loDescription = new Textarea("description");
		loDescription.setText(citem.getDescription());
		loDescription.setLabel("Description");
		loDescription.setStyle("height: 3em");

		Select loComponent = new Select("component");
		loComponent.setLabel("Component");
		loComponent.setValue(citem.getComponent());
		loComponent.addOption("", "");

		if (CollectionUtils.isNotEmpty(citemComponents))
		{
			for (CItemComponentDto citemComponent : citemComponents)
			{
				loComponent.addOption(citemComponent.getClassName(), citemComponent.getClassName());
			}
		}

		Select loEntryPoint = new Select("entryPoint");
		loEntryPoint.setLabel("Entry Point");
		loEntryPoint.setValue(citem.getEntryPoint());
		loEntryPoint.addOption("", "");

		if (CollectionUtils.isNotEmpty(citemEntryPoints))
		{
			for (CItemComponentDto citemloEntryPoint : citemEntryPoints)
			{
				loEntryPoint.addOption(citemloEntryPoint.getUrl(), citemloEntryPoint.getUrl());
			}
		}

		Input loParameter = new Input("parameter");
		loParameter.setLabel("Parameter");
		loParameter.setValue(citem.getParameter());

		Input loFromValid = new Input("date", "fromValid", CmStringUtils.defaultString(citem.getDisplayFromValid(), CmDateFormatUtils.now()));
		loFromValid.setId("fromValid");
		loFromValid.setLabel("Valid From");
		loFromValid.setRequired(true);

		Input loToValid = new Input("date", "toValid", CmStringUtils.defaultString(citem.getDisplayToValid(), MgmtCommonVo.UNLIMITED_DATE_TIME));
		loToValid.setId("toValid");
		loToValid.setLabel("Valid To");
		loToValid.setRequired(true);

		Element loSort = new Input("sort", citem.getSort());
		loSort.setLabel("Sort");
		loSort.setRequired(true);

		Element loVisible = new Checkbox("fgVisible", MgmtCommonVo.YES, citem.getFgVisible());
		loVisible.setLabel("Visible");

		Document document = new Document();
		document.addElement(loType);
		document.addElement(loId);
		document.addElement(loDescription);
		document.addElement(loComponent);
		document.addElement(loEntryPoint);
		document.addElement(loParameter);
		document.addElement(loFromValid);
		document.addElement(loToValid);
		document.addElement(loSort);
		document.addElement(loVisible);

		return document;
	}

	public static Document viewLang(List<String> basenames, List<LangKindDto> langKinds, LangDto lang)
	{
		Document document = new Document();

		document.addDefaultLabelText("Code", lang.getCode());

		Select loBasename = new Select("basename");
		loBasename.setLabel("Basename");
		loBasename.setValue(lang.getBasename());

		for (String basename : basenames)
		{
			boolean isContain = false;

			for (LangKindDto langKind : langKinds)
			{
				if (CmStringUtils.equals(basename, langKind.getBasename()))
				{
					isContain = true;
					break;
				}
			}

			if (isContain)
			{
				loBasename.addOption(basename, basename);
			}
		}

		document.addElement(loBasename);

		if (CollectionUtils.isNotEmpty(langKinds))
		{
			List<LangDetailDto> langDetails = ListUtils.emptyIfNull(lang.getLangDetails());

			for (LangKindDto langKind : langKinds)
			{
				for (LangDetailDto langDetail : langDetails)
				{
					if (langKind.getLkSeq().equals(langDetail.getLkSeq()))
					{
						document.addElement(PlatformLayoutBuildUtils.defaultLabelText(langKind.getName(), langDetail.getText()));
						break;
					}
				}
			}
		}

		return document;
	}
}
