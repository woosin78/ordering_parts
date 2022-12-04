package org.jwebppy.platform.mgmt.authority.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.authority.entity.CItemAuthRlEntity;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;

@NoLogging
@Mapper
public interface AuthorityMapper
{
	public int insertCItemAuthRl(CItemAuthRlEntity citemAuthRl);
	public int updateFgDeleteOfCItemAuthRl(CItemAuthRlEntity citemAuthRl);
	public List<CItemEntity> findCItemAuthorities(CItemSearchDto citemSearch);
	public List<CItemEntity> findSubRoles(CItemSearchDto citemSearch);

}
