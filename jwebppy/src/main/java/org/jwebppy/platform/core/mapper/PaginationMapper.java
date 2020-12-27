package org.jwebppy.platform.core.mapper;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.springframework.stereotype.Repository;

@Repository
public interface PaginationMapper
{
	@NoLogging
	public int getTotalCount();
}
