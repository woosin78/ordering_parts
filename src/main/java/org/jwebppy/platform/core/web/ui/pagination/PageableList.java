package org.jwebppy.platform.core.web.ui.pagination;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageableList<T> implements IPagination
{
	private int pageNumber;
	private int rowPerPage;
	private int totalCount;
	private List<T> list;

	public PageableList() {}

	public PageableList(List<T> list)
	{
		setList(list);
	}

	public void setList(List<T> list)
	{
		this.list = ListUtils.emptyIfNull(list);
		setPagination();
	}

	private void setPagination()
	{
		if (CollectionUtils.isNotEmpty(list))
		{
			IPagination iPagination = (IPagination)list.get(0);

			this.pageNumber = iPagination.getPageNumber();
			this.rowPerPage = iPagination.getRowPerPage();
			this.totalCount = iPagination.getTotalCount();
		}
	}
}
