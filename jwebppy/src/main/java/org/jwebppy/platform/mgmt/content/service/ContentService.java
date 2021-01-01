package org.jwebppy.platform.mgmt.content.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.mapper.ContentMapper;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
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
	public int delete(Integer cSeq)
	{
		CItemEntity cItem = new CItemEntity();
		cItem.setCSeq(cSeq);
		cItem.setFgDelete(PlatformCommonVo.YES);

		return contentMapper.updateFgDelete(cItem);
	}

	public CItemDto getItem(Integer cSeq)
	{
		return CmModelMapperUtils.map(contentMapper.findItem(cSeq), CItemDto.class);
	}

	public List<CItemDto> getAllCItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapAll(contentMapper.findCItems(cItemSearch), CItemDto.class);
	}

	public List<CItemDto> getCItemHierarchy(CItemSearchDto cItemSearch)
	{
		List<CItemDto> cItems = new ArrayList<>();

		makeHierarchy(cItems, cItemSearch);

		return cItems;
	}

	private void makeHierarchy(List<CItemDto> cItems, CItemSearchDto cItemSearch)
	{
		List<CItemDto> subCItems = getAllCItems(cItemSearch);

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

		List<CItemDto> cItems =  getAllCItems(cItemSearch);

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
		cItemSearch.setFgVisible(PlatformCommonVo.YES);

		List<CItemDto> subCItems = getAllCItems(cItemSearch);

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
		return CmModelMapperUtils.mapAll(contentMapper.findMyItems(cItemSearch), CItemDto.class);
	}
}
