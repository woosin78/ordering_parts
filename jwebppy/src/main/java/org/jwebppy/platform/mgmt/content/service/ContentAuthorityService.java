package org.jwebppy.platform.mgmt.content.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.RedisConfig;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemUserRlDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemUserRlEntity;
import org.jwebppy.platform.mgmt.content.mapper.ContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContentAuthorityService extends GeneralService
{
	@Autowired
	private ContentMapper contentMapper;

	//현재 가지고 있는 권한을 모두 삭제 한 후 부여
	@Transactional
	public int save(CItemUserRlDto cItemUserRl)
	{
		Integer uSeq = cItemUserRl.getUSeq();

		if (uSeq != null)
		{
			CItemUserRlEntity cItemUserRlEntity = new CItemUserRlEntity();
			cItemUserRlEntity.setUSeq(uSeq);
			cItemUserRlEntity.setFgDelete(PlatformCommonVo.YES);

			contentMapper.updateFgDeleteOfCItemUserRl(cItemUserRlEntity);

			List<Integer> cSeqs = cItemUserRl.getCSeqs();

			if (CollectionUtils.isNotEmpty(cSeqs))
			{
				for (Integer cSeq : cSeqs)
				{
					cItemUserRlEntity.setCSeq(cSeq);
					cItemUserRlEntity.setFgDelete(PlatformCommonVo.NO);

					contentMapper.insertCItemUserRl(cItemUserRlEntity);
				}

				return 1;
			}
		}

		return 0;
	}

	@Transactional
	public int saveByCItemName(CItemUserRlDto cItemUserRl)
	{
		Integer uSeq = cItemUserRl.getUSeq();

		if (uSeq != null)
		{
			CItemSearchDto cItemSearch = new CItemSearchDto();
			cItemSearch.setName(cItemUserRl.getName());
			cItemSearch.setType(PlatformCommonVo.ROLE);

			List<CItemEntity> cItems = contentMapper.findAllItems(cItemSearch);

			if (CollectionUtils.isNotEmpty(cItems))
			{
				CItemUserRlEntity cItemUserRlEntity = new CItemUserRlEntity();

				cItemUserRlEntity.setUSeq(uSeq);
				cItemUserRlEntity.setCSeq(cItems.get(0).getCSeq());
				cItemUserRlEntity.setName(cItemUserRl.getName());
				cItemUserRlEntity.setFgDelete(PlatformCommonVo.NO);

				return contentMapper.insertCItemUserRl(cItemUserRlEntity);
			}

			return 0;
		}

		return 0;
	}

	@Cacheable(value = RedisConfig.CITEM, key = "#cItemSearch", unless="#result == null")
	public List<CItemDto> getMyItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapAll(contentMapper.findMyItems(cItemSearch), CItemDto.class);
	}

	public CItemDto getMyEntryPoint(CItemSearchDto cItemSearch)
	{
		List<CItemDto> cItems = getMyItems(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItem : cItems)
			{
				if (CmStringUtils.isNotEmpty(cItem.getEntryPoint()))
				{
					return cItem;
				}

				cItemSearch.setPSeq(cItem.getCSeq());
				CItemDto subItem = getEntryPoint(cItemSearch);

				if (subItem != null)
				{
					return subItem;
				}
			}
		}

		return null;
	}

	protected CItemDto getEntryPoint(CItemSearchDto cItemSearch)
	{
		List<CItemEntity> cItems = contentMapper.findCItemsHierarchy(cItemSearch);

		for (CItemEntity cItem : cItems)
		{
			if (CmStringUtils.isNotEmpty(cItem.getEntryPoint()))
			{
				return CmModelMapperUtils.map(cItem, CItemDto.class);
			}

			if (cItem.getSubItemCount() > 0)
			{
				cItemSearch.setPSeq(cItem.getCSeq());
				getEntryPoint(cItemSearch);
			}
		}

		return null;
	}
}
