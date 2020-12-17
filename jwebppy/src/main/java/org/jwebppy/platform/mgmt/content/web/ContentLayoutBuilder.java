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
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;

public class ContentLayoutBuilder
{
	public static Document getGeneralInfo(CItemDto cItem)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Item Type", CmStringUtils.trimToEmpty(cItem.getType()));
		elementMap.put("Name", CmStringUtils.trimToEmpty(cItem.getName()));
		elementMap.put("Description", CmStringUtils.trimToEmpty(cItem.getDescription()));
		elementMap.put("Component", CmStringUtils.trimToEmpty(cItem.getComponent()));
		elementMap.put("Entry Point", CmStringUtils.trimToEmpty(cItem.getEntryPoint()));
		elementMap.put("Parameter", CmStringUtils.trimToEmpty(cItem.getParameter()));
		elementMap.put("Valid From", CmStringUtils.trimToEmpty(cItem.getDisplayFromValid()));
		elementMap.put("Valid To", CmStringUtils.trimToEmpty(cItem.getDisplayToValid()));
		elementMap.put("Sort", CmStringUtils.trimToEmpty(cItem.getSort()));
		elementMap.put("Visible", CmStringUtils.trimToEmpty(cItem.getFgVisible()));
		elementMap.put("Reg. Date", CmStringUtils.trimToEmpty(cItem.getDisplayRegDate()));
		elementMap.put("Reg. Username", CmStringUtils.trimToEmpty(cItem.getRegUsername()));
		elementMap.put("Mod. Date", CmStringUtils.trimToEmpty(cItem.getDisplayModDate()));
		elementMap.put("Mod. Username", CmStringUtils.trimToEmpty(cItem.getModUsername()));

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document getGeneralInfoForm(CItemDto cItem, List<CItemComponentDto> cItemComponents, List<CItemComponentDto> cItemEntryPoints)
	{
		Select loType = new Select("type");
		loType.setLabel("Item Type");
		loType.addAttribute("required");
		loType.setValue(cItem.getType());
		loType.addOption(PlatformCommonVo.PAGE, "PAGE");
		loType.addOption(PlatformCommonVo.MEMU, "MEMU");
		loType.addOption(PlatformCommonVo.ROLE, "ROLE");
		loType.addOption(PlatformCommonVo.FOLDER, "FOLDER");

		Element loId = new Input("name", cItem.getName());
		loId.setLabel("Name");
		loId.addAttribute("required");

		Textarea loDescription = new Textarea("description");
		loDescription.setText(cItem.getDescription());
		loDescription.setLabel("Description");
		loDescription.addAttribute("rows", "3");

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
		loFromValid.addAttribute("required");

		Input loToValid = new Input("date", "toValid", CmStringUtils.defaultString(cItem.getDisplayToValid(), PlatformCommonVo.UNLIMITED_DATE_TIME));
		loToValid.setId("toValid");
		loToValid.setLabel("Valid To");
		loToValid.addAttribute("required");

		Element loSort = new Input("sort", cItem.getSort());
		loSort.setLabel("Sort");
		loSort.addAttribute("required");

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

	public static Document getLang(List<String> basenames, List<LangKindDto> langKinds, LangDto lang)
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
