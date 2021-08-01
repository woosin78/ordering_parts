package org.jwebppy.platform.mgmt.content.service;

import java.util.List;

import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.entity.CItemLangRlEntity;
import org.jwebppy.platform.mgmt.content.mapper.ContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class ContentLangService extends GeneralService
{
	@Autowired
	private ContentMapper contentMapper;

	@Caching(
			evict = {
					@CacheEvict (value = CacheConfig.LANG, allEntries = true),
					@CacheEvict (value = CacheConfig.CITEM, allEntries = true),
			})
	public int save(CItemLangRlDto cItemLangRl)
	{
		if (CollectionUtils.isEmpty(getLangs(cItemLangRl)))
		{
			CItemLangRlEntity cItemLangRlEntity = CmModelMapperUtils.map(cItemLangRl, CItemLangRlEntity.class);

			contentMapper.insertCItemLangRl(cItemLangRlEntity);

			return cItemLangRlEntity.getClrSeq();
		}

		return 0;
	}

	public List<CItemLangRlDto> getLangs(CItemLangRlDto cItemLangRl)
	{
		return CmModelMapperUtils.mapAll(contentMapper.findLangs(cItemLangRl), CItemLangRlDto.class);
	}
}
