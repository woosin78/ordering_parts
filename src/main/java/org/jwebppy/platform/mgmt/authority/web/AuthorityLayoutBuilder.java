package org.jwebppy.platform.mgmt.authority.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Link;
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

public class AuthorityLayoutBuilder
{
	public static Document pageableList(PageableList<CItemDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Type", "one wide");
		thTr.addTextTh("Name", "four wide");
		thTr.addTextTh("Description", "five wide");
		thTr.addTextTh("Users", "one wide");
		thTr.addTextTh("Reg.Username", "two wide");
		thTr.addTextTh("Reg.Date", "two wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<CItemDto> citems = ListUtils.emptyIfNull(pageableList.getList());

		for (CItemDto citem : citems)
		{
			Tr tbTr = new Tr();

			Integer cseq = citem.getCseq();

			tbTr.addDataKeyCheckboxTd("cseq", cseq);
			tbTr.addTextTd(citem.getType().getType());
			tbTr.addDataKeyLinkTd(citem.getName(), cseq);
			tbTr.addTextTd(citem.getDescription());
			//tbTr.addTextTd(citem.getUserCount());
			tbTr.addDataKeyLinkTd(citem.getUserCount(), cseq);
			tbTr.addTextTd(citem.getRegUsername());
			tbTr.addTextTd(citem.getDisplayRegDate());

			tbody.addTr(tbTr);
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody, pageableList));

		return document;
	}

	public static Document viewGeneralInfo(CItemDto citem)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();

		elementMap.put("Name", citem.getName());
		elementMap.put("Description", citem.getDescription());
		elementMap.put("Valid From", citem.getDisplayFromValid());
		elementMap.put("Valid To", citem.getDisplayToValid());
		elementMap.put("Visible", citem.getFgVisible());
		elementMap.put("Sort", citem.getSort());
		elementMap.put("Reg.Date", citem.getDisplayRegDate());
		elementMap.put("Reg.Username", citem.getRegUsername());

		if (citem.getCseq() != null)
		{
			elementMap.put("Mod.Date", citem.getDisplayModDate());
			elementMap.put("Mod.Username", citem.getModUsername());
		}

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document writeGeneralInfo(CItemDto citem)
	{
		Input loName = new Input("name", citem.getName());
		loName.setLabel("Name");
		loName.setStyle("text-transform: uppercase");
		loName.setRequired(true);

		Input loDescription = new Input("description", citem.getDescription());
		loDescription.setLabel("Description");

		Input loFromValid = new Input("date", "fromValid", CmStringUtils.defaultString(citem.getDisplayFromValid(), CmDateFormatUtils.now()));
		loFromValid.setId("fromValid");
		loFromValid.setLabel("Valid From");
		loFromValid.setRequired(true);

		Input loToValid = new Input("date", "toValid", CmStringUtils.defaultString(citem.getDisplayToValid(), CmDateFormatUtils.unlimitDate()));
		loToValid.setId("toValid");
		loToValid.setLabel("Valid To");
		loToValid.setRequired(true);

		Checkbox loFgVisible = new Checkbox("fgVisible", MgmtCommonVo.YES, citem.getFgVisible());
		loFgVisible.setLabel("Visible");

		Input loSort = new Input("sort", CmStringUtils.defaultString(citem.getSort(), 100));
		loSort.setLabel("Sort");
		loSort.setRequired(true);

		Document document = new Document();
		document.addElement(loName);
		document.addElement(loDescription);
		document.addElement(loFromValid);
		document.addElement(loToValid);
		document.addElement(loFgVisible);
		document.addElement(loSort);

		return document;
	}

	public static Document viewAuthority(List<CItemDto> citems)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();

		for (CItemDto citem : ListUtils.emptyIfNull(citems))
		{
			Link loAuthority = new Link(citem.getName());
			loAuthority.setClass("authority");
			loAuthority.setKey(citem.getCseq());
			loAuthority.addAttribute("data-type", citem.getType());

			StringBuilder name = new StringBuilder();
			name.append("[").append(citem.getType().getType()).append( "] ").append(citem.getDescription());

			elementMap.put(name.toString(), loAuthority);
		}

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document writeAuthority(List<CItemDto> citems)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTh(new Th("Name"));
		thTr.addTh(new Th("Description"));

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		if (CollectionUtils.isNotEmpty(citems))
		{
			for (CItemDto citemDto : citems)
			{
				Tr tbTr = new Tr();
				tbTr.addDataKeyCheckboxTd("cseq", citemDto.getCseq());
				tbTr.addTextTd(citemDto.getName());
				tbTr.addTextTd(citemDto.getDescription());

				tbody.addTr(tbTr);
			}
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody));

		return document;
	}
}
