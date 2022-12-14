package org.jwebppy.platform.mgmt.i18n.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.common.service.MgmtGeneralService;
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
import org.jwebppy.platform.mgmt.i18n.mapper.LangDetailObjectMapper;
import org.jwebppy.platform.mgmt.i18n.mapper.LangKindObjectMapper;
import org.jwebppy.platform.mgmt.i18n.mapper.LangMapper;
import org.jwebppy.platform.mgmt.i18n.mapper.LangObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LangService extends MgmtGeneralService
{
	@Autowired
	private ContentService contentService;

	@Autowired
	private LangMapper langMapper;

	public int save(LangDto lang)
	{
		LangEntity langEntity = null;
		Integer lseq = null;

		if (lang.getLseq() != null)
		{
			langEntity = langMapper.findLang(lang);
		}

		if (langEntity == null)
		{
			langEntity = LangObjectMapper.INSTANCE.toEntity(lang);

			if (CmStringUtils.isEmpty(langEntity.getSeq()))
			{
				langEntity.setSeq(Long.toString(System.currentTimeMillis()));
			}

			langMapper.insertLang(langEntity);

			lseq = langEntity.getLseq();
		}
		else
		{
			langMapper.updateLang(LangObjectMapper.INSTANCE.toEntity(lang));
			lseq = langEntity.getLseq();
		}

		for (LangDetailDto langDetail : lang.getLangDetails())
		{
			langDetail.setLseq(lseq);

			LangDetailEntity langDetailEntity = LangDetailObjectMapper.INSTANCE.toEntity(langDetail);

			if (langMapper.findLangDetail(langDetail) == null)
			{
				langMapper.insertLangDetail(langDetailEntity);
			}
			else
			{
				langMapper.updateLangDetail(langDetailEntity);
			}
		}

		return lseq;
	}

	public int delete(LangDto lang)
	{
		langMapper.updateLangDetailFgDelete(lang);

		return langMapper.updateLangFgDelete(lang);
	}

	public LangDto getLangByLSeq(Integer lseq)
	{
		LangDto lang = new LangDto();
		lang.setLseq(lseq);

		return CmModelMapperUtils.mapToDto(LangObjectMapper.INSTANCE, langMapper.findLang(lang));
	}

	public LangDto getLang(LangDto lang)
	{
		return CmModelMapperUtils.mapToDto(LangObjectMapper.INSTANCE, langMapper.findLang(lang));
	}

	public List<LangDto> getPageableLangs(LangSearchDto langSearchDto)
	{
		return CmModelMapperUtils.mapToDto(LangObjectMapper.INSTANCE, langMapper.findPageableLangs(langSearchDto));
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
		return CmModelMapperUtils.mapToDto(LangDetailObjectMapper.INSTANCE, langMapper.findLangDetails(langSearch));
	}

	public List<LangKindDto> getLangKinds(LangKindDto langKind)
	{
		return LangKindObjectMapper.INSTANCE.toDtoList(langMapper.findLangKinds(langKind));
	}

	@Cacheable(value = CacheConfig.LANG, key = "{#basename, #cseq, #lang}", unless="#result == null")
	public String getCitemText(String basename, Integer cseq, String lang)
	{
		if (basename == null || cseq == null)
		{
			return null;
		}

		List<LangDetailDto> langDetails = CmModelMapperUtils.mapToDto(LangDetailObjectMapper.INSTANCE, langMapper.findLangDetailsCItems(CItemSearchDto.builder()
				.cseq(cseq)
				.basename(basename)
				.lang(lang)
				.build()));

		if (CollectionUtils.isNotEmpty(langDetails))
		{
			return langDetails.get(0).getText();
		}

		CItemDto citem = contentService.getCitem(cseq);

		if (citem != null)
		{
			return citem.getName();
		}

		return null;
	}

	public List<String> getBasenames()
	{
		/*
		List<LangKindEntity> langKinds = langMapper.findLangKinds(null);

		if (CollectionUtils.isNotEmpty(langKinds))
		{
			List<String> basenames = new ArrayList<>();

			for (LangKindEntity langKind: langKinds)
			{
				String basename = langKind.getBasename();

				if (!basenames.contains(basename))
				{
					basenames.add(basename);
				}
			}

			return basenames;
		}

		return null;
		*/
		List<String> basements = new ArrayList<>();

		basements.add("PLTF");
		basements.add("PTL");
		basements.add("HQP");
		basements.add("EUP");
		basements.add("UKP");

		return basements;
	}

	public String getDefaultLang(String basename)
	{
		LangKindDto pLangKind = new LangKindDto();
		pLangKind.setBasename(PlatformConfigVo.DEFAULT_BASENAME);

		List<LangKindEntity> langKinds = langMapper.findLangKinds(pLangKind);

		if (CollectionUtils.isNotEmpty(langKinds))
		{
			return langKinds.get(0).getCode();
		}

		return null;
	}

	public boolean isDuplicated(LangDto lang)
	{
		if (langMapper.findLang(lang) == null)
		{
			return false;
		}

		if (lang.getLseq() != null)
		{
			return false;
		}

		return true;
	}

	public List<LangDto> exportData(LangSearchDto langSearch)
	{
		return CmModelMapperUtils.mapToDto(LangObjectMapper.INSTANCE, langMapper.findLangs(langSearch));
	}
}
