package org.jwebppy.platform.mgmt.content.mapper;

import java.util.List;

import org.jwebppy.config.RedisConfig;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemLangRlEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemUserRlEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface ContentMapper
{
	@CacheEvict(value = RedisConfig.CITEM, allEntries = true)
	public int insert(CItemEntity cItem);

	@CacheEvict(value = RedisConfig.CITEM, allEntries = true)
	public int insertCItemUserRl(CItemUserRlEntity cItemUserRl);

	@CacheEvict(value = RedisConfig.CITEM, allEntries = true)
	public int insertCItemLangRl(CItemLangRlEntity cItemLangRl);

	@CacheEvict(value = RedisConfig.CITEM, allEntries = true)
	public int update(CItemEntity cItem);

	@CacheEvict(value = RedisConfig.CITEM, allEntries = true)
	public int updateFgDelete(CItemEntity cItem);

	@CacheEvict(value = RedisConfig.CITEM, allEntries = true)
	public int updateCItemLangRl(CItemLangRlEntity cItemLangRl);

	@CacheEvict(value = RedisConfig.CITEM, allEntries = true)
	public int updateFgDeleteOfCItemUserRl(CItemUserRlEntity cItemUserRl);

	@CacheEvict(value = RedisConfig.CITEM, allEntries = true)
	public CItemEntity findItem(Integer cSeq);

	public List<CItemEntity> findMyItems(CItemSearchDto cItemSearch);

	public List<CItemEntity> findAllItems(CItemSearchDto cItemSearch);

	@Cacheable(value = RedisConfig.CITEM, key = "#cItemSearch", unless="#result == null")
	public List<CItemEntity> findCItemsHierarchy(CItemSearchDto cItemSearch);

	public List<CItemEntity> findHigherLevelCItems(CItemSearchDto cItemSearch);

	public List<CItemLangRlEntity> findLangs(CItemLangRlDto cItemLangRl);
}
