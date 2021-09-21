package org.jwebppy.platform.mgmt.content.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
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
	public static Document viewGeneralInfo(CItemDto cItem)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Item Type", cItem.getType().getType());
		elementMap.put("Name", cItem.getName());
		elementMap.put("Description", cItem.getDescription());
		elementMap.put("Component", cItem.getComponent());
		elementMap.put("Entry Point", cItem.getEntryPoint());
		elementMap.put("Parameter", cItem.getParameter());
		elementMap.put("Valid From", cItem.getDisplayFromValid());
		elementMap.put("Valid To", cItem.getDisplayToValid());
		elementMap.put("Sort", cItem.getSort());
		elementMap.put("Visible", cItem.getFgVisible());
		elementMap.put("Reg. Date", cItem.getDisplayRegDate());
		elementMap.put("Reg. Username", cItem.getRegUsername());
		elementMap.put("Mod. Date", cItem.getDisplayModDate());
		elementMap.put("Mod. Username", cItem.getModUsername());

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document writeGeneralInfo(CItemDto cItem, List<CItemComponentDto> cItemComponents, List<CItemComponentDto> cItemEntryPoints)
	{
		Select loType = new Select("type");
		loType.setLabel("Item Type");
		loType.setRequired(true);

		if (cItem.getCSeq() != null)
		{
			loType.addOption(cItem.getType(), cItem.getType().getType());
		}
		else
		{
			for (CItemType cItemType: CItemType.values2())
			{
				loType.addOption(cItemType.name(), cItemType.getType());
			}
		}

		Input loId = new Input("name", cItem.getName());
		loId.setLabel("Name");
		loId.setStyle("text-transform: uppercase");
		loId.setRequired(true);

		Textarea loDescription = new Textarea("description");
		loDescription.setText(cItem.getDescription());
		loDescription.setLabel("Description");
		loDescription.setStyle("height: 3em");

		Select loComponent = new Select("component");
		loComponent.setLabel("Component");
		loComponent.setValue(cItem.getComponent());
		loComponent.addOption("", "");

		if (CollectionUtils.isNotEmpty(cItemComponents))
		{
			for (CItemComponentDto cItemComponent : cItemComponents)
			{
				loComponent.addOption(cItemComponent.getClassName(), cItemComponent.getClassName());
			}
		}

		Select loEntryPoint = new Select("entryPoint");
		loEntryPoint.setLabel("Entry Point");
		loEntryPoint.setValue(cItem.getEntryPoint());
		loEntryPoint.addOption("", "");

		if (CollectionUtils.isNotEmpty(cItemEntryPoints))
		{
			for (CItemComponentDto cItemloEntryPoint : cItemEntryPoints)
			{
				loEntryPoint.addOption(cItemloEntryPoint.getUrl(), cItemloEntryPoint.getUrl());
			}
		}

		Input loParameter = new Input("parameter");
		loParameter.setLabel("Parameter");
		loParameter.setValue(cItem.getParameter());

		Input loFromValid = new Input("date", "fromValid", CmStringUtils.defaultString(cItem.getDisplayFromValid(), CmDateFormatUtils.now()));
		loFromValid.setId("fromValid");
		loFromValid.setLabel("Valid From");
		loFromValid.setRequired(true);

		Input loToValid = new Input("date", "toValid", CmStringUtils.defaultString(cItem.getDisplayToValid(), PlatformCommonVo.UNLIMITED_DATE_TIME));
		loToValid.setId("toValid");
		loToValid.setLabel("Valid To");
		loToValid.setRequired(true);

		Element loSort = new Input("sort", cItem.getSort());
		loSort.setLabel("Sort");
		loSort.setRequired(true);

		Element loVisible = new Checkbox("fgVisible", PlatformCommonVo.YES, cItem.getFgVisible());
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

		Select loCorp = new Select("basename");
		loCorp.setLabel("Basename");
		loCorp.setValue(lang.getBasename());

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
				loCorp.addOption(basename, basename);
			}
		}

		document.addElement(loCorp);

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
