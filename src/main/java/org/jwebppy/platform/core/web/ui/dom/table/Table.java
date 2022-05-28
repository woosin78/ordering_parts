package org.jwebppy.platform.core.web.ui.dom.table;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Attribute;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Table extends Element implements IPagination
{
	private int pageNumber;
	private int rowPerPage;
	private int totalCount;

	public Table()
	{
		super("TABLE");
		setDefaultTableClass();
	}

	public Table(IPagination pagination)
	{
		super("TABLE");
		setDefaultTableClass();

		setPagination(pagination);
	}

	public Table(Element thead, Element tbody)
	{
		super("TABLE");
		setDefaultTableClass();

		addElement(thead);
		addElement(tbody);
	}

	public Table(Element thead, Element tbody, IPagination pagination)
	{
		super("TABLE");
		setDefaultTableClass();

		addElement(thead);
		addElement(tbody);

		setPagination(pagination);
	}

	private void setDefaultTableClass()
	{
		addAttribute("CLASS", "ui table selectable compact celled sortable teal");
	}

	public void addThead(Element thead)
	{
		addElement(thead);
	}

	public void addTbody(Element tbody)
	{
		addElement(tbody);
	}

	@Override
	public int getPageNumber()
	{
		return pageNumber;
	}

	@Override
	public int getRowPerPage()
	{
		return rowPerPage;
	}

	@Override
	public int getTotalCount()
	{
		return totalCount;
	}

	public void setPagination(IPagination pagination)
	{
		if (pagination != null)
		{
			addAttribute(new Attribute("data-page-number", CmStringUtils.trimToEmpty(pagination.getPageNumber())));
			addAttribute(new Attribute("data-row-per-page", CmStringUtils.trimToEmpty(pagination.getRowPerPage())));
			addAttribute(new Attribute("data-total-count", CmStringUtils.trimToEmpty(pagination.getTotalCount())));
		}
	}
}
