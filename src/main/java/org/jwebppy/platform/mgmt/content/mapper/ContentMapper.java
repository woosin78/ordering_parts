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
	public int insert(CItemEntity citem);
	public int insertCitemUserRl(CItemUserRlEntity citemUserRl);
	public int insertCitemLangRl(CItemLangRlEntity citemLangRl);
	public int update(CItemEntity citem);
	public int updateCItemLangRl(CItemLangRlEntity citemLangRl);
	public int delete(CItemEntity citem);
	public int deleteCitemUserRl(CItemUserRlEntity citemUserRl);
	public CItemEntity findCitem(CItemSearchDto citemSearch);
	public List<CItemEntity> findMyCitems(CItemSearchDto citemSearch);
	public List<CItemEntity> findCitems(CItemSearchDto citemSearch);
	public List<CItemEntity> findPageCitems(CItemSearchDto citemSearch);
	public List<CItemLangRlEntity> findCitemLangRls(CItemLangRlDto citemLangRl);
	public List<CItemEntity> findCitemsForTree(CItemSearchDto citemSearch);
}
