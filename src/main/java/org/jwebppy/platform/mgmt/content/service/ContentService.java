package org.jwebppy.platform.mgmt.content.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.content.entity.CItemEntity;
import org.jwebppy.platform.mgmt.content.mapper.CItemObjectMapper;
import org.jwebppy.platform.mgmt.content.mapper.ContentMapper;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
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
	private ContentLangService contentLangService;

	@Autowired
	private ContentMapper contentMapper;

	@Autowired
	private LangService langService;

	public int create(CItemDto citem)
	{
		CItemEntity citemEntity = CmModelMapperUtils.mapToEntity(CItemObjectMapper.INSTANCE, citem);

		contentMapper.insert(citemEntity);

		return citemEntity.getCseq();
	}

	public int modify(CItemDto citem)
	{
		return contentMapper.update(CmModelMapperUtils.mapToEntity(CItemObjectMapper.INSTANCE, citem));
	}

	public int save(CItemDto citem)
	{
		if (citem.getCseq() == null)
		{
			return create(citem);
		}
		else
		{
			return modify(citem);
		}
	}

	public int delete(List<Integer> cseqs)
	{
		if (CollectionUtils.isNotEmpty(cseqs))
		{
			for (Integer cseq: cseqs)
			{
				if (cseq == null)
				{
					continue;
				}

				delete(cseq);
			}
		}

		return 1;
	}

	public int delete(Integer cseq)
	{
		CItemDto citem = getCitem(cseq);

		if (ObjectUtils.isNotEmpty(citem))
		{
			if (citem.getType().equals(CItemType.G))
			{
				return contentMapper.delete(CItemEntity.builder()
						.cseq(cseq)
						.build());
			}

			List<Map<String, Object>> citems = getCitemHierarchy2(CItemSearchDto.builder()
					.cseq(cseq)
					.build());

			if (CollectionUtils.isNotEmpty(citems))
			{
				Map<String, Object> citemMap = citems.get(0);

				if (MapUtils.isNotEmpty(citemMap))
				{
					delete(citemMap);
				}
			}

			return 1;
		}

		return 0;
	}

	private void delete(Map<String, Object> citemMap)
	{
		CItemEntity citem = new CItemEntity();
		citem.setCseq((Integer)citemMap.get("KEY"));
		citem.setFgDelete(MgmtCommonVo.YES);

		contentMapper.delete(CItemEntity.builder()
				.cseq((Integer)citemMap.get("KEY"))
				.build());

		List<Map<String, Object>> subCitems = (List<Map<String, Object>>)citemMap.get("SUB_ITEMS");

		if (CollectionUtils.isNotEmpty(subCitems))
		{
			for (Map<String, Object> subItemMap: subCitems)
			{
				delete(subItemMap);
			}
		}
	}

	public int copy(Integer cseq, Integer pseq, String fgCopyWithSubItems)
	{
		if (ObjectUtils.isEmpty(cseq)|| ObjectUtils.isEmpty(pseq))
		{
			return -1;
		}

		List<Map<String, Object>> citems = getCitemHierarchy2(CItemSearchDto.builder()
				.cseq(cseq)
				.build());

		if (CollectionUtils.isNotEmpty(citems))
		{
			if (CmStringUtils.equals(fgCopyWithSubItems, MgmtCommonVo.YES))
			{
				copy(cseq, pseq, citems.get(0));
			}
			else
			{
				copyCItem(cseq, pseq);
			}
		}

		return 1;
	}

	private void copy(Integer cseq, Integer pseq, Map<String, Object> citemMap)
	{
		pseq = copyCItem(cseq, pseq);

		List<Map<String, Object>> subCitems = (List<Map<String, Object>>)citemMap.get("SUB_ITEMS");

		if (CollectionUtils.isNotEmpty(subCitems))
		{
			for (Map<String, Object> subItemMap: subCitems)
			{
				copy((Integer)subItemMap.get("KEY"), pseq, subItemMap);
			}
		}
	}

	private Integer copyCItem(Integer cseq, Integer pseq)
	{
		CItemDto citem = getCitem(cseq);

		citem.setCseq(null);
		citem.setPseq(pseq);
		citem.setFromValid(LocalDateTime.now());
		citem.setToValid(LocalDateTime.now().plusYears(100));
		citem.setModDate(null);
		citem.setModUsername(null);

		if (citem.getType().equals(CItemType.R))
		{
			citem.setName(citem.getName() + "_" + CmDateFormatUtils.now());
		}

		Integer nCseq = create(citem);

		if (ObjectUtils.isNotEmpty(nCseq))
		{
			//다국어 copy
			copyCItemLang(cseq, nCseq);
		}

		return nCseq;
	}

	public Integer copyCItemLang(Integer sCseq, Integer nCseq)
	{
		List<CItemLangRlDto> citemLangRls = contentLangService.getCitemLangRls(CItemLangRlDto.builder()
				.cseq(sCseq)
				.basename(PlatformConfigVo.DEFAULT_BASENAME)
				.build());

		if (CollectionUtils.isNotEmpty(citemLangRls))
		{
			CItemLangRlDto citemLangRl = citemLangRls.get(0);

			LangDto slang = langService.getLangByLSeq(citemLangRl.getLseq());

			List<LangDetailDto> nlangDetails = new ArrayList<LangDetailDto>();

			for (LangDetailDto slangDetail: ListUtils.emptyIfNull(slang.getLangDetails()))
			{
				nlangDetails.add(LangDetailDto.builder()
						.lkSeq(slangDetail.getLkSeq())
						.text(slangDetail.getText())
						.build());
			}

			Integer nLseq = langService.save(LangDto.builder()
					.basename(slang.getBasename())
					.type(slang.getType())
					.langDetails(nlangDetails)
					.build());

			if (ObjectUtils.isNotEmpty(nLseq))
			{
				return contentLangService.save(CItemLangRlDto.builder()
						.cseq(nCseq)
						.lseq(nLseq)
						.build());
			}
		}

		return null;
	}

	public int move(Integer cseq, Integer pseq)
	{
		CItemDto citem = getCitem(cseq);
		citem.setPseq(pseq);

		return contentMapper.update(CmModelMapperUtils.mapToEntity(CItemObjectMapper.INSTANCE, citem));
	}

	public CItemDto getRoot()
	{
		return getCitem(CItemSearchDto.builder()
				.name(PlatformConfigVo.CITEM_ROOT)
				.build());
	}

	public CItemDto getCitem(Integer cseq)
	{
		return getCitem(CItemSearchDto.builder()
				.cseq(cseq)
				.build());
	}

	public CItemDto getCitem(CItemSearchDto citemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findCitem(citemSearch));
	}

	public List<CItemDto> getCitems(CItemSearchDto citemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findCitems(citemSearch));
	}

	public List<CItemDto> getPageableCitems(CItemSearchDto citemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findPageCitems(citemSearch));
	}

	@Cacheable(value = CacheConfig.CITEM, unless="#result == null")
	public List<CItemDto> getCitemHierarchy(CItemSearchDto citemSearch)
	{
		List<CItemDto> citems = new ArrayList<>();

		makeHierarchy(citems, citemSearch);

		return citems;
	}

	private void makeHierarchy(List<CItemDto> citems, CItemSearchDto citemSearch)
	{
		List<CItemDto> subCitems = getCitems(citemSearch);

		if (CollectionUtils.isNotEmpty(subCitems))
		{
			for (CItemDto subCItem: subCitems)
			{
				citems.add(subCItem);

				makeHierarchy(citems, CItemSearchDto.builder()
						.pseq(subCItem.getCseq())
						.fgVisible(MgmtCommonVo.YES)
						.build());
			}
		}
	}

	public List<CItemDto> getCitemsFormTree(CItemSearchDto citemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findCitemsForTree(citemSearch));
	}

	@Cacheable(value = CacheConfig.CITEM, unless="#result == null")
	public List<Map<String, Object>> getCitemHierarchy2(CItemSearchDto citemSearch)
	{
		citemSearch.setBasename(PlatformConfigVo.DEFAULT_BASENAME);
		citemSearch.setLang(UserAuthenticationUtils.getUserDetails().getLanguage());

		List<CItemDto> citems = getCitemsFormTree(citemSearch);

		if (CollectionUtils.isNotEmpty(citems))
		{
			List<Map<String, Object>> hierarchy = new LinkedList<>();

			CItemDto citem = citems.get(0);

			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("KEY", citem.getCseq());
			itemMap.put("P_KEY", CmStringUtils.trimToEmpty(citem.getPseq()));
			itemMap.put("NAME", CmStringUtils.defaultIfEmpty(citem.getName2(), citem.getName()));
			itemMap.put("TYPE", citem.getType().getType());
			itemMap.put("LAUNCH_TYPE", CmStringUtils.trimToEmpty(citem.getLaunchType()));
			itemMap.put("WIDTH", CmStringUtils.trimToEmpty(citem.getScrWidth()));
			itemMap.put("HEIGHT", CmStringUtils.trimToEmpty(citem.getScrHeight()));
			itemMap.put("SUB_ITEMS", getSubItems(citem.getCseq(), citems));

			hierarchy.add(itemMap);

			return hierarchy;
		}

		return Collections.emptyList();
	}

	public List<Map<String, Object>> getSubItems(Integer cseq, List<CItemDto> citems)
	{
		List<Map<String, Object>> subItems = new LinkedList<>();

		for (int i=0, size=citems.size(); i<size; i++)
		{
			CItemDto subCItem = citems.get(i);

			if (cseq.equals(subCItem.getPseq()))
			{
				Map<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("KEY", subCItem.getCseq());
				itemMap.put("P_KEY", CmStringUtils.trimToEmpty(subCItem.getPseq()));
				itemMap.put("NAME", CmStringUtils.defaultIfEmpty(subCItem.getName2(), subCItem.getName()));
				itemMap.put("TYPE", subCItem.getType().getType());
				itemMap.put("LAUNCH_TYPE", CmStringUtils.trimToEmpty(subCItem.getLaunchType()));
				itemMap.put("WIDTH", CmStringUtils.trimToEmpty(subCItem.getScrWidth()));
				itemMap.put("HEIGHT", CmStringUtils.trimToEmpty(subCItem.getScrHeight()));
				itemMap.put("SUB_ITEMS", getSubItems(subCItem.getCseq(), citems));

				subItems.add(itemMap);
			}
		}

		return subItems;
	}
}
