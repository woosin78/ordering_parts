package org.jwebppy.platform.mgmt.logging.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.logging.entity.SapRfcEntity;

@NoLogging
@Mapper
public interface SapRfcMapper
{
	public SapRfcEntity findByName(String name);
}
