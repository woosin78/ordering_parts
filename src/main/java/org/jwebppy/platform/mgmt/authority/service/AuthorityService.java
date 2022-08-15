package org.jwebppy.platform.mgmt.authority.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.authority.dto.CItemAuthRlDto;
import org.jwebppy.platform.mgmt.authority.entity.CItemAuthRlEntity;
import org.jwebppy.platform.mgmt.authority.mapper.AuthorityMapper;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.mapper.CItemObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthorityService
{
	@Autowired
	private AuthorityMapper authorityMapper;

	public List<CItemDto> getCItemAuthorities(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, authorityMapper.findCItemAuthorities(cItemSearch));
	}

	@Cacheable(value = CacheConfig.CITEM, unless="#result == null")
	public List<CItemDto> getSubRoles(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, authorityMapper.findSubRoles(cItemSearch));
	}

	//현재 가지고 있는 권한을 모두 삭제 한 후 부여
	@CacheEvict (value = CacheConfig.CITEM, allEntries = true)
	public int save(CItemAuthRlDto cItemAuthRl)
	{
		Integer pSeq = cItemAuthRl.getPSeq();

		if (pSeq != null)
		{
			CItemAuthRlEntity cItemUserRlEntity = new CItemAuthRlEntity();
			cItemUserRlEntity.setPSeq(pSeq);
			cItemUserRlEntity.setFgDelete(PlatformCommonVo.YES);

			authorityMapper.updateFgDeleteOfCItemAuthRl(cItemUserRlEntity);

			List<Integer> cSeqs = cItemAuthRl.getCSeqs();

			if (CollectionUtils.isNotEmpty(cSeqs))
			{
				for (Integer cSeq : cSeqs)
				{
					if (cSeq == null || cSeq.equals(pSeq))
					{
						continue;
					}

					cItemUserRlEntity.setCSeq(cSeq);
					cItemUserRlEntity.setFgDelete(PlatformCommonVo.NO);
					cItemUserRlEntity.setSort(100);

					authorityMapper.insertCItemAuthRl(cItemUserRlEntity);
				}

				return 1;
			}
		}

		return 0;
	}
}
