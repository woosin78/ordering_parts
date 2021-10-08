package org.jwebppy.platform.mgmt.conn_resource.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceSearchDto;
import org.jwebppy.platform.mgmt.conn_resource.entity.SapConnResourceEntity;

@NoLogging
@Mapper
public interface SapConnResourceMapper
{
	public int insert(SapConnResourceEntity sapConnResource);
	public int update(SapConnResourceEntity sapConnResource);
	public int updateFgDelete(SapConnResourceEntity sapConnResource);
	public int updateFgUse(SapConnResourceEntity sapConnResource);
	public SapConnResourceEntity findSapConnResource(Integer scrSeq);
	public List<SapConnResourceEntity> findPageSapConnResources(SapConnResourceSearchDto sapConnResourceSearch);
	public List<SapConnResourceEntity> findAvailableSapConnResources();
}

