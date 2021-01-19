package org.jwebppy.platform.mgmt.content.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.mapper.ContentMapper;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContentService extends GeneralService
{
	@Autowired
	private ContentMapper contentMapper;

	@Autowired
	private LangService langService;

	@Transactional
	public int create(CItemDto cItem)
	{
		CItemEntity cItemEntity = CmModelMapperUtils.map(cItem, CItemEntity.class);

		contentMapper.insert(cItemEntity);

		return cItemEntity.getCSeq();
	}

	@Transactional
	@CacheEvict (value = CacheConfig.CITEM, allEntries = true)
	public int modify(CItemDto cItem)
	{
		return contentMapper.update(CmModelMapperUtils.map(cItem, CItemEntity.class));
	}

	@Transactional
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

	@Transactional
	@CacheEvict (value = CacheConfig.CITEM, allEntries = true)
	public int delete(Integer cSeq)
	{
//		CItemEntity cItem = new CItemEntity();
//		cItem.setCSeq(cSeq);
//		cItem.setFgDelete(PlatformCommonVo.YES);
//
//		contentMapper.updateFgDelete(cItem);

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

		contentMapper.updateFgDelete(cItem);

		List<Map<String, Object>> subItems = (List<Map<String, Object>>)cItemMap.get("SUB_ITEMS");

		if (CollectionUtils.isNotEmpty(subItems))
		{
			for (Map<String, Object> subItemMap: subItems)
			{
				delete(subItemMap);
			}
		}
	}

	@Transactional
	@CacheEvict (value = CacheConfig.CITEM, allEntries = true)
	public int copy(Integer cSeq, Integer pSeq, String fgCopyAll)
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
			if (CmStringUtils.equals(fgCopyAll, PlatformCommonVo.YES))
			{
				copyCItem(cSeq, pSeq);
			}
			else
			{
				copy(cSeq, pSeq, cItems.get(0));
			}
		}

		return 1;
	}

	private void copy(Integer cSeq, Integer pSeq, Map<String, Object> cItemMap)
	{
		pSeq = copyCItem(cSeq, pSeq);

		List<Map<String, Object>> subItems = (List<Map<String, Object>>)cItemMap.get("SUB_ITEMS");

		if (CollectionUtils.isNotEmpty(subItems))
		{
			for (Map<String, Object> subItemMap: subItems)
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
		cItem.setModDate(null);
		cItem.setModUsername(null);

		if (PlatformCommonVo.ROLE.equals(cItem.getType().toString()))
		{
			cItem.setName(cItem.getName() + "_" + CmDateFormatUtils.now());
		}

		return create(cItem);
	}

	public CItemDto getCItem(Integer cSeq)
	{
		return CmModelMapperUtils.map(contentMapper.findCItem(cSeq), CItemDto.class);
	}

	public List<CItemDto> getCItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapAll(contentMapper.findCItems(cItemSearch), CItemDto.class);
	}

	public List<CItemDto> getPageableCItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapAll(contentMapper.findPageCItems(cItemSearch), CItemDto.class);
	}

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

	public List<Map<String, Object>> getCItemHierarchy2(CItemSearchDto cItemSearch)
	{
		List<Map<String, Object>> hierarchy = new LinkedList<>();

		cItemSearch.setTypes(new String[] {PlatformCommonVo.FOLDER, PlatformCommonVo.ROLE, PlatformCommonVo.MEMU, PlatformCommonVo.PAGE});

		List<CItemDto> cItems = getCItems(cItemSearch);

		if (CollectionUtils.isNotEmpty(cItems))
		{
			CItemDto cItem = cItems.get(0);

			Map<String, Object> itemMap = new HashMap<>();

			itemMap.put("KEY", cItem.getCSeq());
			itemMap.put("P_KEY", cItem.getPSeq());
			itemMap.put("NAME", langService.getCItemText(PlatformCommonVo.DEFAULT_BASENAME, cItem.getCSeq(), UserAuthenticationUtils.getUserDetails().getLanguage()));
			itemMap.put("TYPE", cItem.getType().getType());
			itemMap.put("SUB_ITEMS", getSubItems(cItem.getCSeq()));

			hierarchy.add(itemMap);
		}

		return hierarchy;
	}

	private List<Map<String, Object>> getSubItems(Integer pSeq)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setPSeq(pSeq);
		cItemSearch.setTypes(new String[] {PlatformCommonVo.FOLDER, PlatformCommonVo.ROLE, PlatformCommonVo.MEMU, PlatformCommonVo.PAGE});

		List<CItemDto> subCItems = getCItems(cItemSearch);

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			List<Map<String, Object>> hierarchy = new LinkedList<>();

			for (CItemDto subCItem: subCItems)
			{
				Map<String, Object> itemMap = new HashMap<>();

				itemMap.put("KEY", subCItem.getCSeq());
				itemMap.put("P_KEY", subCItem.getPSeq());
				itemMap.put("NAME", langService.getCItemText(PlatformCommonVo.DEFAULT_BASENAME, subCItem.getCSeq(), UserAuthenticationUtils.getUserDetails().getLanguage()));
				itemMap.put("TYPE", subCItem.getType().getType());
				itemMap.put("SUB_ITEMS", getSubItems(subCItem.getCSeq()));

				hierarchy.add(itemMap);
			}

			return hierarchy;
		}

		return null;
	}

	public List<CItemDto> getMyItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapAll(contentMapper.findMyCItems(cItemSearch), CItemDto.class);
	}
}
