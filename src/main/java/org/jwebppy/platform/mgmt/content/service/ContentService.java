package org.jwebppy.platform.mgmt.content.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.mapper.CItemObjectMapper;
import org.jwebppy.platform.mgmt.content.mapper.ContentMapper;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContentService extends GeneralService
{
	@Autowired
	private ContentMapper contentMapper;

	@Autowired
	private LangService langService;

	public int create(CItemDto cItem)
	{
		CItemEntity cItemEntity = CmModelMapperUtils.mapToEntity(CItemObjectMapper.INSTANCE, cItem);

		contentMapper.insert(cItemEntity);

		return cItemEntity.getCSeq();
	}

	public int modify(CItemDto cItem)
	{
		return contentMapper.update(CmModelMapperUtils.mapToEntity(CItemObjectMapper.INSTANCE, cItem));
	}

	public int save(CItemDto cItem)
	{
		if (cItem.getCSeq() == null)
		{
			return create(cItem);
		}
		else
		{
			return modify(cItem);
		}
	}

	public int delete(List<Integer> cSeqs)
	{
		if (CollectionUtils.isNotEmpty(cSeqs))
		{
			for (Integer cSeq: cSeqs)
			{
				if (cSeq == null)
				{
					continue;
				}

				delete(cSeq);
			}
		}

		return 1;
	}

	public int delete(Integer cSeq)
	{
		CItemDto cItem = getCItem(cSeq);

		if (cItem.getType().equals(CItemType.G))
		{
			CItemEntity cItemEntity = new CItemEntity();
			cItemEntity.setCSeq(cSeq);
			cItemEntity.setFgDelete(PlatformCommonVo.YES);

			return contentMapper.delete(cItemEntity);
		}

		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setCSeq(cSeq);

		List<Map<String, Object>> cItems = getCItemHierarchy2(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			Map<String, Object> cItemMap = cItems.get(0);

			if (MapUtils.isNotEmpty(cItemMap))
			{
				delete(cItemMap);
			}
		}

		return 1;
	}

	private void delete(Map<String, Object> cItemMap)
	{
		CItemEntity cItem = new CItemEntity();
		cItem.setCSeq((Integer)cItemMap.get("KEY"));
		cItem.setFgDelete(PlatformCommonVo.YES);

		contentMapper.delete(cItem);

		List<Map<String, Object>> subCItems = (List<Map<String, Object>>)cItemMap.get("SUB_ITEMS");

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			for (Map<String, Object> subItemMap: subCItems)
			{
				delete(subItemMap);
			}
		}
	}

	public int copy(Integer cSeq, Integer pSeq, String fgCopyWithSubItems)
	{
		if (cSeq == null || pSeq == null)
		{
			return -1;
		}

		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setCSeq(cSeq);

		List<Map<String, Object>> cItems = getCItemHierarchy2(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			if (CmStringUtils.equals(fgCopyWithSubItems, PlatformCommonVo.YES))
			{
				copy(cSeq, pSeq, cItems.get(0));
			}
			else
			{
				copyCItem(cSeq, pSeq);
			}
		}

		return 1;
	}

	private void copy(Integer cSeq, Integer pSeq, Map<String, Object> cItemMap)
	{
		pSeq = copyCItem(cSeq, pSeq);

		List<Map<String, Object>> subCItems = (List<Map<String, Object>>)cItemMap.get("SUB_ITEMS");

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			for (Map<String, Object> subItemMap: subCItems)
			{
				copy((Integer)subItemMap.get("KEY"), pSeq, subItemMap);
			}
		}
	}

	private Integer copyCItem(Integer cSeq, Integer pSeq)
	{
		CItemDto cItem = getCItem(cSeq);

		cItem.setCSeq(null);
		cItem.setPSeq(pSeq);
		cItem.setFromValid(LocalDateTime.now());
		cItem.setToValid(LocalDateTime.now().plusYears(100));
		cItem.setModDate(null);
		cItem.setModUsername(null);

		if (cItem.getType().equals(CItemType.R))
		{
			cItem.setName(cItem.getName() + "_" + CmDateFormatUtils.now());
		}

		return create(cItem);
	}

	public int move(Integer cSeq, Integer pSeq)
	{
		CItemDto cItem = getCItem(cSeq);
		cItem.setPSeq(pSeq);

		return contentMapper.update(CmModelMapperUtils.mapToEntity(CItemObjectMapper.INSTANCE, cItem));
	}

	public CItemDto getRoot()
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setName(PlatformConfigVo.CITEM_ROOT);

		return getCItem(cItemSearch);
	}

	public CItemDto getCItem(Integer cSeq)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setCSeq(cSeq);

		return getCItem(cItemSearch);
	}

	public CItemDto getCItem(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findCItem(cItemSearch));
	}

	public List<CItemDto> getCItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findCItems(cItemSearch));
	}

	public List<CItemDto> getPageableCItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findPageCItems(cItemSearch));
	}

	@Cacheable(keyGenerator = "cacheKeyGenerator", value = CacheConfig.CITEM, unless="#result == null")
	public List<CItemDto> getCItemHierarchy(CItemSearchDto cItemSearch)
	{
		List<CItemDto> cItems = new ArrayList<>();

		makeHierarchy(cItems, cItemSearch);

		return cItems;
	}

	private void makeHierarchy(List<CItemDto> cItems, CItemSearchDto cItemSearch)
	{
		List<CItemDto> subCItems = getCItems(cItemSearch);

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			for (CItemDto subCItem: subCItems)
			{
				cItems.add(subCItem);

				CItemSearchDto subCItemSearch = new CItemSearchDto();
				subCItemSearch.setPSeq(subCItem.getCSeq());
				subCItemSearch.setFgVisible(PlatformCommonVo.YES);

				makeHierarchy(cItems, subCItemSearch);
			}
		}
	}

	@Cacheable(keyGenerator = "cacheKeyGenerator", value = CacheConfig.CITEM, unless="#result == null")
	public List<Map<String, Object>> getCItemHierarchy2(CItemSearchDto cItemSearch)
	{
		cItemSearch.setTypes(new CItemType[] {CItemType.F, CItemType.R, CItemType.M, CItemType.P});

		List<CItemDto> cItems = getCItems(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			List<Map<String, Object>> hierarchy = new LinkedList<>();

			CItemDto cItem = cItems.get(0);

			Map<String, Object> itemMap = new HashMap<>();

			itemMap.put("KEY", cItem.getCSeq());
			itemMap.put("P_KEY", cItem.getPSeq());
			itemMap.put("NAME", langService.getCItemText(PlatformConfigVo.DEFAULT_BASENAME, cItem.getCSeq(), UserAuthenticationUtils.getUserDetails().getLanguage()));
			itemMap.put("TYPE", cItem.getType().getType());
			itemMap.put("SUB_ITEMS", getSubItems(cItem.getCSeq()));

			hierarchy.add(itemMap);

			return hierarchy;
		}

		return Collections.emptyList();
	}

	private List<Map<String, Object>> getSubItems(Integer pSeq)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setPSeq(pSeq);
		cItemSearch.setTypes(new CItemType[] {CItemType.F, CItemType.R, CItemType.M, CItemType.P});

		List<CItemDto> subCItems = getCItems(cItemSearch);

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			List<Map<String, Object>> hierarchy = new LinkedList<>();

			for (CItemDto subCItem: subCItems)
			{
				Map<String, Object> itemMap = new HashMap<>();

				itemMap.put("KEY", subCItem.getCSeq());
				itemMap.put("P_KEY", subCItem.getPSeq());
				itemMap.put("NAME", langService.getCItemText(PlatformConfigVo.DEFAULT_BASENAME, subCItem.getCSeq(), UserAuthenticationUtils.getUserDetails().getLanguage()));
				itemMap.put("TYPE", subCItem.getType().getType());
				itemMap.put("SUB_ITEMS", getSubItems(subCItem.getCSeq()));

				hierarchy.add(itemMap);
			}

			return hierarchy;
		}

		return Collections.emptyList();
	}

	public List<CItemDto> getMyItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findMyCItems(cItemSearch));
	}
}