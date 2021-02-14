package org.jwebppy.platform.mgmt.board.web;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;

import com.nhncorp.lucy.security.xss.XssPreventer;

public class BoardContentLayoutBuilder
{
	public static Document getList(PageableList<BoardContentDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("No.", "one wide");
		thTr.addTextTh("Title");
		thTr.addTextTh("Views", "one wide");
		thTr.addTextTh("Writer", "two wide");
		thTr.addTextTh("Reg.Date", "two wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		List<BoardContentDto> boardContents = ListUtils.emptyIfNull(pageableList.getList());

		for (BoardContentDto boardContent : boardContents)
		{
			Tr tbTr = new Tr();
			tbTr.addDataKeyCheckboxTd("bcSeq", boardContent.getBcSeq());
			tbTr.addTextTd(boardContent.getNo());
			tbTr.addDataKeyLinkTd(XssPreventer.escape(boardContent.getTitle()), boardContent.getBcSeq());
			tbTr.addTextTd(boardContent.getViews());
			tbTr.addTextTd(boardContent.getWriter());
			tbTr.addTextTd(boardContent.getDisplayRegDate());

			tbody.addTr(tbTr);
		}

		Table table = new Table();
		table.addThead(thead);
		table.addTbody(tbody);
		table.setPagination(pageableList);

		Document document = new Document();
		document.addElement(table);

		return document;
	}
}
