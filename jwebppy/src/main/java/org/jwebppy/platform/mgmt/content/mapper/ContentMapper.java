package org.jwebppy.platform.mgmt.content.mapper;

import java.util.List;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemLangRlEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemUserRlEntity;
import org.springframework.cache.annotation.CacheEvict;

public interface ContentMapper
{
	@CacheEvict(value = PlatformConfigVo.CITEM, allEntries = true)
	public int insert(CItemEntity cItem);

	@CacheEvict(value = PlatformConfigVo.CITEM, allEntries = true)
	public int insertCItemUserRl(CItemUserRlEntity cItemUserRl);

	@CacheEvict(value = PlatformConfigVo.CITEM, allEntries = true)
	public int insertCItemLangRl(CItemLangRlEntity cItemLangRl);

	@CacheEvict(value = PlatformConfigVo.CITEM, allEntries = true)
	public int update(CItemEntity cItem);

	@CacheEvict(value = PlatformConfigVo.CITEM, allEntries = true)
	public int updateFgDelete(CItemEntity cItem);

	@CacheEvict(value = PlatformConfigVo.CITEM, allEntries = true)
	public int updateCItemLangRl(CItemLangRlEntity cItemLangRl);

	@CacheEvict(value = PlatformConfigVo.CITEM, allEntries = true)
	public int updateFgDeleteOfCItemUserRl(CItemUserRlEntity cItemUserRl);

	@CacheEvict(value = PlatformConfigVo.CITEM, allEntries = true)
	@NoLogging
	public CItemEntity findItem(Integer cSeq);

	@NoLogging
	public List<CItemEntity> findMyItems(CItemSearchDto cItemSearch);

	@NoLogging
	public List<CItemEntity> findCItems(CItemSearchDto cItemSearch);

	@NoLogging
	public List<CItemLangRlEntity> findLangs(CItemLangRlDto cItemLangRl);
}
