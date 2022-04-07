package org.jwebppy.platform.mgmt.content.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.authority.service.AuthorityService;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.content.dto.CItemUserRlDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemUserRlEntity;
import org.jwebppy.platform.mgmt.content.mapper.CItemObjectMapper;
import org.jwebppy.platform.mgmt.content.mapper.ContentMapper;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContentAuthorityService extends GeneralService
{
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private ContentMapper contentMapper;

	@Autowired
	private LangService langService;

	public int save(CItemUserRlDto cItemUserRl)
	{
		Integer uSeq = cItemUserRl.getUSeq();

		if (uSeq != null)
		{
			CItemUserRlEntity cItemUserRlEntity = new CItemUserRlEntity();
			cItemUserRlEntity.setUSeq(uSeq);
			cItemUserRlEntity.setFgDelete(PlatformCommonVo.YES);

			contentMapper.deleteCItemUserRl(cItemUserRlEntity);

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

	public int saveByCItemName(CItemUserRlDto cItemUserRl)
	{
		Integer uSeq = cItemUserRl.getUSeq();

		if (uSeq != null)
		{
			CItemSearchDto cItemSearch = new CItemSearchDto();
			cItemSearch.setName(cItemUserRl.getName());
			cItemSearch.setTypes(new CItemType[] { CItemType.R, CItemType.G  } );

			List<CItemEntity> cItems = contentMapper.findCItems(cItemSearch);

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

	public List<CItemDto> getMyCItems(Integer uSeq)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setUSeq(uSeq);

		return getMyCItems(cItemSearch);
	}

	public List<CItemDto> getMyCItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findMyCItems(cItemSearch));
	}

	@Cacheable(value = CacheConfig.CITEM, unless="#result == null")
	public List<CItemDto> getMyCItemHierarchy(CItemSearchDto cItemSearch)
	{
		List<CItemDto> cItems = getMyCItems(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			List<CItemDto> hierarchy = new LinkedList<>();

			List<CItemDto> roles = new ArrayList<>();

			for (CItemDto cItem : cItems)
			{
				if (CItemType.G.equals(cItem.getType()))
				{
					CItemSearchDto cItemSearch2 = new CItemSearchDto();
					cItemSearch2.setUSeq(cItemSearch.getUSeq());
					cItemSearch2.setPSeq(cItem.getCSeq());

					List<CItemDto> subRoles = authorityService.getSubRoles(cItemSearch2);

					if (CollectionUtils.isNotEmpty(subRoles))
					{
						for (CItemDto subRole : subRoles)
						{
							roles.add(subRole);
						}
					}
				}
				else
				{
					roles.add(cItem);
				}
			}

			String lang = cItemSearch.getLang();

			if (CmStringUtils.isEmpty(lang))
			{
				if (UserAuthenticationUtils.isAuthenticated())
				{
					lang = UserAuthenticationUtils.getUserDetails().getLanguage();
				}
				else
				{
					lang = langService.getDefaultLang(PlatformConfigVo.DEFAULT_BASENAME);
				}
			}

			for (CItemDto role : roles)
			{
				role.setName2(langService.getCItemText(PlatformConfigVo.DEFAULT_BASENAME, role.getCSeq(), lang));
				role.setSubCItems(getSubCItems(role.getCSeq(), lang));

				hierarchy.add(role);
			}

			return hierarchy;
		}

		return Collections.emptyList();
	}

	@Cacheable(keyGenerator = "cacheKeyGenerator", value = CacheConfig.CITEM, unless="#result == null")
	public List<CItemDto> getSubCItems(Integer cSeq, String lang)
	{
		List<CItemDto> cItems = new LinkedList<>();

		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setPSeq(cSeq);
		cItemSearch.setFgVisible(PlatformCommonVo.YES);

		List<CItemDto> subCItems = contentService.getCItemHierarchy(cItemSearch);

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			for (CItemDto subCItem: subCItems)
			{
				Integer subCSeq = subCItem.getCSeq();

				if (cSeq.equals(subCItem.getPSeq()))
				{
					subCItem.setName2(langService.getCItemText(PlatformConfigVo.DEFAULT_BASENAME, subCSeq, lang));
					subCItem.setSubCItems(getSubCItems(subCSeq, lang));

					cItems.add(subCItem);
				}
			}
		}

		return cItems;
	}

	public CItemDto getMyEntryPoint(CItemSearchDto cItemSearch)
	{
		List<CItemDto> cItems = getMyCItemHierarchy(cItemSearch);

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
		List<CItemDto> cItems = contentService.getCItemHierarchy(cItemSearch);

		for (CItemDto cItem: cItems)
		{
			if (CmStringUtils.isNotEmpty(cItem.getEntryPoint()))
			{
				return cItem;
			}
		}

		return null;
	}
}
