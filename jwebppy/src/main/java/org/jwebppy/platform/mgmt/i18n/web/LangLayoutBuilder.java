package org.jwebppy.platform.mgmt.i18n.web;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.form.InputHidden;
import org.jwebppy.platform.core.web.ui.dom.form.Select;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangType;

public class LangLayoutBuilder
{
	public static Document getList(PageableList<LangDto> pageableList, List<LangKindDto> langKinds)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Code", "three wide");

		if (CollectionUtils.isNotEmpty(langKinds))
		{
			for (LangKindDto langKindDto : langKinds)
			{
				thTr.addTextTh(langKindDto.getName());
			}
		}

		Element thead = new Thead();
		thead.addElement(thTr);

		Element tbody = new Tbody();
		List<LangDto> langs = ListUtils.emptyIfNull(pageableList.getList());

		for (LangDto lang : langs)
		{
			Tr tbTr = new Tr();
			tbTr.addDataKeyCheckboxTd("lSeq", lang.getLSeq());
			tbTr.addDataKeyLinkTd(lang.getMessageCode(), lang.getLSeq());

			Collection<LangDetailDto> LangDetails = CollectionUtils.emptyIfNull(lang.getLangDetails());

			if (CollectionUtils.isNotEmpty(langKinds))
			{
				for (LangKindDto langKind : langKinds)
				{
					boolean isFound = false;

					for (LangDetailDto langDetail : LangDetails)
					{
						if (langKind.getLkSeq().equals(langDetail.getLkSeq()))
						{
							isFound = true;

							tbTr.addTextTd(langDetail.getText());
							break;
						}
					}

					if (!isFound)
					{
						tbTr.addTextTd("");
					}
				}
			}

			tbody.addElement(tbTr);
		}

		Table table = new Table();
		table.addThead(thead);
		table.addTbody(tbody);
		table.setPagination(pageableList);

		Document document = new Document();
		document.addElement(table);

		return document;
	}

	public static Document getLangForm(List<String> basenames, List<LangKindDto> langKinds, LangDto lang, String from)
	{
		Document document = new Document();

		if (CmStringUtils.equals(from, "CONTENT"))
		{
			document.addElement(new InputHidden("basename", lang.getBasename()));
			document.addElement(new InputHidden("type", PlatformCommonVo.LABEL));
		}
		else
		{
			Select loCorp = new Select("basename");
			loCorp.setLabel("Basename");
			loCorp.setRequired(true);
			loCorp.setValue(lang.getBasename());

			for (String basename : basenames)
			{
				loCorp.addOption(basename, basename);
			}

			document.addElement(loCorp);

			if (CmStringUtils.isNotEmpty(lang.getType()) && CmStringUtils.isNotEmpty(lang.getSeq()))
			{
				document.addElement(new InputHidden("type", lang.getType()));
			}
			else
			{
				Select loType = new Select("type");
				loType.setLabel("Type");
				loType.setRequired(true);
				loType.setValue(lang.getType());

				for (LangType langType: LangType.values())
				{
					loType.addOption(langType.name(), langType.getType());
				}

				document.addElement(loType);
			}

			Element loSeq = new Input("seq", lang.getSeq());
			loSeq.setLabel("Suffix");
			loSeq.addAttribute("autofocus");
			loSeq.addAttribute("style", "text-transform:uppercase");
			loSeq.setRequired(true);

			document.addElement(loSeq);
		}

		if (CollectionUtils.isNotEmpty(langKinds))
		{
			List<LangDetailDto> langDetails = ListUtils.emptyIfNull(lang.getLangDetails());

			for (LangKindDto langKind : langKinds)
			{
				Integer lkSeq = langKind.getLkSeq();

				document.addElement(new InputHidden("lkSeq", lkSeq));

				Input loText = new Input("text");
				loText.setLabel(langKind.getName());

				document.addElement(loText);

				for (LangDetailDto langDetail : langDetails)
				{
					if (lkSeq.equals(langDetail.getLkSeq()))
					{
						loText.setValue(langDetail.getText());
						break;
					}
				}
			}
		}

		return document;
	}
}
