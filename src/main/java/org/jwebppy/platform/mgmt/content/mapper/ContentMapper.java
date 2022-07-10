package org.jwebppy.platform.mgmt.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemLangRlEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemUserRlEntity;

@NoLogging
@Mapper
public interface ContentMapper
{
	public int insert(CItemEntity cItem);
	public int insertCItemUserRl(CItemUserRlEntity cItemUserRl);
	public int insertCItemLangRl(CItemLangRlEntity cItemLangRl);
	public int update(CItemEntity cItem);
	public int updateCItemLangRl(CItemLangRlEntity cItemLangRl);
	public int delete(CItemEntity cItem);
	public int deleteCItemUserRl(CItemUserRlEntity cItemUserRl);
	public CItemEntity findCItem(CItemSearchDto cItemSearch);
	public List<CItemEntity> findMyCItems(CItemSearchDto cItemSearch);
	public List<CItemEntity> findCItems(CItemSearchDto cItemSearch);
	public List<CItemEntity> findPageCItems(CItemSearchDto cItemSearch);
	public List<CItemLangRlEntity> findCItemLangRls(CItemLangRlDto cItemLangRl);
	public List<CItemEntity> findCItemsForTree(CItemSearchDto cItemSearch);
}
