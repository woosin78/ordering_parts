package org.jwebppy.platform.mgmt.i18n.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.MgmtGeneralService;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangSearchDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangType;
import org.jwebppy.platform.mgmt.i18n.entity.LangDetailEntity;
import org.jwebppy.platform.mgmt.i18n.entity.LangEntity;
import org.jwebppy.platform.mgmt.i18n.entity.LangKindEntity;
import org.jwebppy.platform.mgmt.i18n.mapper.LangMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LangService extends MgmtGeneralService
{
	@Autowired
	private ContentService contentService;

	@Autowired
	private LangMapper langMapper;

	@Transactional
	@CacheEvict (value = CacheConfig.LANG, allEntries = true)
	public int save(LangDto lang)
	{
		LangEntity langEntity = null;
		Integer lSeq = null;

		if (lang.getLSeq() != null)
		{
			langEntity = langMapper.findLang(lang);
		}

		if (langEntity == null)
		{
			langEntity = CmModelMapperUtils.map(lang, LangEntity.class);

			if (CmStringUtils.isEmpty(langEntity.getSeq()))
			{
				langEntity.setSeq(Long.toString(System.currentTimeMillis()));
			}

			langMapper.insertLang(langEntity);

			lSeq = langEntity.getLSeq();
		}
		else
		{
			langMapper.updateLang(CmModelMapperUtils.map(lang, LangEntity.class));
			lSeq = langEntity.getLSeq();
		}

		for (LangDetailDto langDetail : lang.getLangDetails())
		{
			langDetail.setLSeq(lSeq);

			LangDetailEntity langDetailEntity = CmModelMapperUtils.map(langDetail, LangDetailEntity.class);

			if (langMapper.findLangDetail(langDetail) == null)
			{
				langMapper.insertLangDetail(langDetailEntity);
			}
			else
			{
				langMapper.updateLangDetail(langDetailEntity);
			}
		}

		return lSeq;
	}

	@Transactional
	@CacheEvict (value = CacheConfig.LANG, allEntries = true)
	public int delete(LangDto lang)
	{
		langMapper.updateLangDetailFgDelete(lang);

		return langMapper.updateLangFgDelete(lang);
	}

	public LangDto getLangByLSeq(Integer lSeq)
	{
		LangDto lang = new LangDto();
		lang.setLSeq(lSeq);

		return CmModelMapperUtils.map(langMapper.findLang(lang), LangDto.class);
	}

	public LangDto getLang(LangDto lang)
	{
		return CmModelMapperUtils.map(langMapper.findLang(lang), LangDto.class);
	}

	public List<LangDto> getPageableLangs(LangSearchDto langSearchDto)
	{
		return CmModelMapperUtils.mapAll(langMapper.findLangs(langSearchDto), LangDto.class);
	}

	public String getText(String key)
	{
		return getLangDetailByCode(key, UserAuthenticationUtils.getUserDetails().getLanguage()).getText();
	}

	@Cacheable(value = CacheConfig.LANG, key = "{#key, #locale}", unless="#result == null")
	public LangDetailDto getLangDetailByCode(String key, String locale)
	{
		String[] codes = key.split("_");

		if (codes != null && codes.length >= 3)
		{
			StringBuffer suffix = new StringBuffer();
			int length = codes.length;

			if (length == 3)
			{
				suffix.append(codes[2]);
			}
			else
			{
				for (int i=2; i<length; i++)
				{
					if (i > 2)
					{
						suffix.append("_");
					}

					suffix.append(codes[i]);
				}
			}

			LangSearchDto langSearch = new LangSearchDto();
			langSearch.setBasename(codes[0]);
			langSearch.setType(LangType.valueOf(codes[1]));
			langSearch.setSeq(suffix.toString());
			langSearch.setCode(locale);

			List<LangDetailDto> langDetails = getLangDetails(langSearch);

			if (CollectionUtils.isNotEmpty(langDetails))
			{
				return langDetails.get(0);
			}
		}

		return null;
	}

	public List<LangDetailDto> getLangDetails(LangSearchDto langSearch)
	{
		return CmModelMapperUtils.mapAll(langMapper.findLangDetails(langSearch), LangDetailDto.class);
	}

	public List<LangKindDto> getLangKinds(LangKindDto langKind)
	{
		return CmModelMapperUtils.mapAll(langMapper.findLangKinds(langKind), LangKindDto.class);
	}

	@Cacheable(value = CacheConfig.LANG, key = "{#basename, #cSeq, #lang}", unless="#result == null")
	public String getCItemText(String basename, Integer cSeq, String lang)
	{
		if (basename == null || cSeq == null)
		{
			return null;
		}

		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setCSeq(cSeq);
		cItemSearch.setBasename(basename);
		cItemSearch.setLang(lang);

		List<LangDetailDto> langDetails = CmModelMapperUtils.mapAll(langMapper.findLangDetailsCItems(cItemSearch), LangDetailDto.class);

		if (CollectionUtils.isNotEmpty(langDetails))
		{
			return langDetails.get(0).getText();
		}

		CItemDto cItem = contentService.getCItem(cSeq);

		if (cItem != null)
		{
			return cItem.getName();
		}

		return null;
	}

	public List<String> getBasenames()
	{
		List<LangKindEntity> langKinds = langMapper.findLangKinds(null);

		if (CollectionUtils.isNotEmpty(langKinds))
		{
			List<String> basenames = new ArrayList<>();

			for (LangKindEntity langKind: langKinds)
			{
				basenames.add(langKind.getBasename());
			}

			return basenames;
		}

		return null;
	}

	public String getDefaultLang(String basename)
	{
		LangKindDto pLangKind = new LangKindDto();
		pLangKind.setBasename(PlatformCommonVo.DEFAULT_BASENAME);

		List<LangKindEntity> langKinds = langMapper.findLangKinds(pLangKind);

		if (CollectionUtils.isNotEmpty(langKinds))
		{
			return langKinds.get(0).getCode();
		}

		return null;
	}
}
