package org.jwebppy.platform.mgmt.content.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemUserRlDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.entity.CItemUserRlEntity;
import org.jwebppy.platform.mgmt.content.mapper.ContentMapper;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContentAuthorityService extends GeneralService
{
	@Autowired
	private ContentService contentService;

	@Autowired
	private ContentMapper contentMapper;

	@Autowired
	private LangService langService;

	//현재 가지고 있는 권한을 모두 삭제 한 후 부여
	@Transactional
	@CacheEvict (value = CacheConfig.CITEM, allEntries = true)
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

	@Cacheable(value = CacheConfig.CITEM, key = "#cItemSearch.uSeq", unless="#result == null")
	public List<CItemDto> getMyItemHierarchy(CItemSearchDto cItemSearch)
	{
		List<CItemDto> hierarchy = new LinkedList<>();

		List<CItemDto> cItems = CmModelMapperUtils.mapAll(contentMapper.findMyCItems(cItemSearch), CItemDto.class);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			String lang = cItemSearch.getLang();

			if (CmStringUtils.isEmpty(lang))
			{
				if (UserAuthenticationUtils.isLogin())
				{
					lang = UserAuthenticationUtils.getUserDetails().getLanguage();
				}
				else
				{
					lang = langService.getDefaultLang(PlatformCommonVo.DEFAULT_BASENAME);
				}
			}

			for (CItemDto cItem : cItems)
			{
				cItem.setName2(langService.getCItemText(PlatformCommonVo.DEFAULT_BASENAME, cItem.getCSeq(), lang));
				cItem.setSubCItems(getSubItems(cItem.getCSeq(), lang));

				hierarchy.add(cItem);
			}
		}

		return hierarchy;
	}

	private List<CItemDto> getSubItems(Integer cSeq, String lang)
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
					subCItem.setName2(langService.getCItemText(PlatformCommonVo.DEFAULT_BASENAME, subCSeq, lang));
					subCItem.setSubCItems(getSubItems(subCSeq, lang));

					cItems.add(subCItem);
				}
			}
		}

		return cItems;
	}

	public CItemDto getMyEntryPoint(CItemSearchDto cItemSearch)
	{
		List<CItemDto> cItems = getMyItemHierarchy(cItemSearch);

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
