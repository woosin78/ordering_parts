package org.jwebppy.platform.core.util;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.entity.GeneralEntity;

public class PaginationUtils
{
	public static void setTotalCount(List<GeneralEntity> list, int totalCount)
	{
		if (CollectionUtils.isNotEmpty(list))
		{
			for (GeneralEntity general: list)
			{
				general.setTotalCount(totalCount);
			}
		}
	}
}
