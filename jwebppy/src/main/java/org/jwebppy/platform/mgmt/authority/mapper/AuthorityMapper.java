package org.jwebppy.platform.mgmt.authority.mapper;

import java.util.List;

import org.jwebppy.platform.mgmt.authority.dto.CItemAuthRlEntity;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;

public interface AuthorityMapper
{
	public int insertCItemAuthRl(CItemAuthRlEntity cItemAuthRl);
	public int updateFgDeleteOfCItemAuthRl(CItemAuthRlEntity cItemAuthRl);
	public List<CItemEntity> findCItemAuthorities(CItemSearchDto cItemSearch);
	public List<CItemEntity> findSubRoles(CItemSearchDto cItemSearch);

}
