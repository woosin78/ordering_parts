package org.jwebppy.platform.mgmt.authority.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
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

	public List<CItemDto> getCitemAuthorities(CItemSearchDto citemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, authorityMapper.findCItemAuthorities(citemSearch));
	}

	@Cacheable(value = CacheConfig.CITEM, unless="#result == null")
	public List<CItemDto> getSubRoles(CItemSearchDto citemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, authorityMapper.findSubRoles(citemSearch));
	}

	//현재 가지고 있는 권한을 모두 삭제 한 후 부여
	@CacheEvict (value = CacheConfig.CITEM, allEntries = true)
	public int save(CItemAuthRlDto citemAuthRl)
	{
		Integer pseq = citemAuthRl.getPseq();

		if (pseq != null)
		{
			CItemAuthRlEntity citemUserRlEntity = new CItemAuthRlEntity();
			citemUserRlEntity.setPseq(pseq);
			citemUserRlEntity.setFgDelete(MgmtCommonVo.YES);

			authorityMapper.updateFgDeleteOfCItemAuthRl(citemUserRlEntity);

			List<Integer> cseqs = citemAuthRl.getCseqs();

			if (CollectionUtils.isNotEmpty(cseqs))
			{
				for (Integer cseq : cseqs)
				{
					if (cseq == null || cseq.equals(pseq))
					{
						continue;
					}

					citemUserRlEntity.setCseq(cseq);
					citemUserRlEntity.setFgDelete(MgmtCommonVo.NO);
					citemUserRlEntity.setSort(100);

					authorityMapper.insertCItemAuthRl(citemUserRlEntity);
				}

				return 1;
			}
		}

		return 0;
	}
}
