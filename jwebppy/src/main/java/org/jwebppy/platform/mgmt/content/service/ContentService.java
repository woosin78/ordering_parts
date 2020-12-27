package org.jwebppy.platform.mgmt.content.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

	public List<CItemDto> getAllItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapAll(contentMapper.findAllItems(cItemSearch), CItemDto.class);
	}

	public List<CItemDto> getCItemHierarchy(CItemSearchDto cItemSearch)
	{
		List<CItemEntity> cItems = new ArrayList<>();

		makeHierarchy(cItems, cItemSearch);

		return CmModelMapperUtils.mapAll(cItems, CItemDto.class);
	}

	private void makeHierarchy(List<CItemEntity> cItems, CItemSearchDto cItemSearch)
	{
		List<CItemEntity> subCItems = contentMapper.findAllCItems(cItemSearch);

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			for (CItemEntity subCItem: subCItems)
			{
				cItems.add(subCItem);

				CItemSearchDto subCItemSearch = new CItemSearchDto();
				subCItemSearch.setPSeq(subCItem.getCSeq());
				subCItemSearch.setFgVisible(PlatformCommonVo.YES);

				makeHierarchy(cItems, subCItemSearch);
			}
		}
	}

	public Map<String, Object> makeHierarchy(CItemSearchDto cItemSearch)
	{
		List<CItemDto> cItems = getCItemHierarchy(cItemSearch);

		Map<String, Object> itemMap = new LinkedHashMap<>();

		if (!CollectionUtils.isEmpty(cItems))
		{
			CItemDto cItem = cItems.get(0);

			itemMap.put("KEY", cItem.getCSeq());
			itemMap.put("P_KEY", cItem.getPSeq());
			itemMap.put("NAME", langService.getCItemText("PLTF", cItem.getCSeq(), UserAuthenticationUtils.getUserDetails().getLanguage()));
			itemMap.put("TYPE", getTypeName(cItem.getType().toString()));
			itemMap.put("SUB_ITEMS", getSubItems(cItems, cItem.getCSeq()));
		}

		return itemMap;
	}

	private List<Map<String, Object>> getSubItems(List<CItemDto> cItems, Integer cSeq)
	{
		List<Map<String, Object>> subItems = new LinkedList<>();

		String lang = UserAuthenticationUtils.getUserDetails().getLanguage();

		for (CItemDto cItem : cItems)
		{
			if (cSeq.equals(cItem.getPSeq()))
			{
				Map<String, Object> itemMap = new LinkedHashMap<>();
				itemMap.put("KEY", cItem.getCSeq());
				itemMap.put("P_KEY", cItem.getPSeq());
				itemMap.put("NAME", langService.getCItemText("PLTF", cItem.getCSeq(), lang));
				itemMap.put("TYPE", getTypeName(cItem.getType().toString()));

				if (cItem.getSubItemCount() > 0)
				{
					itemMap.put("SUB_ITEMS", getSubItems(cItems, cItem.getCSeq()));
				}

				subItems.add(itemMap);
			}
		}

		return subItems;
	}

	public List<CItemDto> getMyItems(CItemSearchDto cItemSearch)
	{
		return CmModelMapperUtils.mapAll(contentMapper.findMyItems(cItemSearch), CItemDto.class);
	}

	public String getTypeName(String type)
	{
		Map<String, String> typeNameMap = new HashMap<>();
		typeNameMap.put("F", "FOLDER");
		typeNameMap.put("R", "ROLE");
		typeNameMap.put("M", "MENU");
		typeNameMap.put("P", "PAGE");

		return typeNameMap.get(type);
	}
}
