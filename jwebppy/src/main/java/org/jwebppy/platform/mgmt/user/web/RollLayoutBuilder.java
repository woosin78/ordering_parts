package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;

public class RollLayoutBuilder
{
	public static Document getList(PageableList<CItemDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Name", "two wide");
		thTr.addTextTh("Description", "three wide");
		thTr.addTextTh("Valid From", "two wide");
		thTr.addTextTh("Valid To", "two wide");
		thTr.addTextTh("Reg.Date", "two wide");
		thTr.addTextTh("Reg.Username", "two wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<CItemDto> cItems = pageableList.getList();

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItem : cItems)
			{
				Tr tbTr = new Tr();
				tbTr.addDataKeyCheckboxTd("cSeq", cItem.getCSeq());
				tbTr.addTextTd(cItem.getName());
				tbTr.addTextTd(cItem.getDescription());
				tbTr.addTextTd(cItem.getFromValid());
				tbTr.addTextTd(cItem.getToValid());
				tbTr.addTextTd(cItem.getRegUsername());
				tbTr.addTextTd(cItem.getRegDate());

				tbody.addTr(tbTr);
			}
		}

		Table table = new Table(pageableList);
		table.addThead(thead);
		table.addTbody(tbody);

		Document document = new Document();
		document.add(table);

		return document;
	}
}
