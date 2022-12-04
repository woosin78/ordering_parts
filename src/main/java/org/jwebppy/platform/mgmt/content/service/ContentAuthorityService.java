package org.jwebppy.platform.mgmt.content.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.authority.service.AuthorityService;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
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

	public int save(CItemUserRlDto citemUserRl)
	{
		Integer useq = citemUserRl.getUseq();

		if (useq != null)
		{
			contentMapper.deleteCitemUserRl(CItemUserRlEntity.builder()
					.useq(useq)
					.fgDelete(MgmtCommonVo.YES)
					.build());

			List<Integer> cseqs = citemUserRl.getCseqs();

			if (CollectionUtils.isNotEmpty(cseqs))
			{
				for (Integer cseq : cseqs)
				{
					contentMapper.insertCitemUserRl(CItemUserRlEntity.builder()
							.useq(useq)
							.cseq(cseq)
							.fgDelete(MgmtCommonVo.NO)
							.build());
				}

				return 1;
			}
		}

		return 0;
	}

	public int saveByCItemName(CItemUserRlDto citemUserRl)
	{
		Integer useq = citemUserRl.getUseq();

		if (useq != null)
		{
			List<CItemEntity> citems = contentMapper.findCitems(CItemSearchDto.builder()
					.name(citemUserRl.getName())
					.types(new CItemType[] { CItemType.R, CItemType.G  })
					.build());

			if (CollectionUtils.isNotEmpty(citems))
			{
				CItemUserRlEntity citemUserRlEntity = CItemUserRlEntity.builder()
						.useq(useq)
						.cseq(citems.get(0).getCseq())
						.name(citemUserRl.getName())
						.fgDelete(MgmtCommonVo.NO)
						.build();

				return contentMapper.insertCitemUserRl(citemUserRlEntity);
			}

			return 0;
		}

		return 0;
	}

	public List<CItemDto> getMyCitems(Integer useq)
	{
		return getMyCitems(CItemSearchDto.builder()
				.useq(useq)
				.build());
	}

	public List<CItemDto> getMyCitems(CItemSearchDto citemSearch)
	{
		return CmModelMapperUtils.mapToDto(CItemObjectMapper.INSTANCE, contentMapper.findMyCitems(citemSearch));
	}

	public List<CItemDto> getMyCitemHierarchy(CItemSearchDto citemSearch)
	{
		List<CItemDto> myCitems = getMyCitems(citemSearch);

		if (CollectionUtils.isNotEmpty(myCitems))
		{
			List<CItemDto> hierarchy = new LinkedList<>();

			List<CItemDto> roles = new ArrayList<>();

			for (CItemDto citem : myCitems)
			{
				if (CItemType.G.equals(citem.getType()))
				{
					List<CItemDto> subRoles = authorityService.getSubRoles(CItemSearchDto.builder()
							.useq(citemSearch.getUseq())
							.pseq(citem.getCseq())
							.build());

					if (CollectionUtils.isNotEmpty(subRoles))
					{
						roles.addAll(subRoles);
					}
				}
				else
				{
					roles.add(citem);
				}
			}

			String lang = citemSearch.getLang();

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

			citemSearch.setBasename(PlatformConfigVo.DEFAULT_BASENAME);
			citemSearch.setLang(lang);
			citemSearch.setFgVisible(MgmtCommonVo.YES);

			List<CItemDto> citems = ListUtils.emptyIfNull(contentService.getCitemsFormTree(citemSearch));

			for (CItemDto role : roles)
			{
				role.setName2(langService.getCitemText(PlatformConfigVo.DEFAULT_BASENAME, role.getCseq(), lang));
				role.setSubCitems(getSubCItems2(role.getCseq(), citems));

				hierarchy.add(role);
			}

			return hierarchy;
		}

		return Collections.emptyList();
	}

	public List<CItemDto> getSubCItems2(Integer cseq, List<CItemDto> citems)
	{
		List<CItemDto> subItems = new LinkedList<>();

		for (int i=0, size=citems.size(); i<size; i++)
		{
			CItemDto subCItem = citems.get(i);

			if (cseq.equals(subCItem.getPseq()))
			{
				subCItem.setSubCitems(getSubCItems2(subCItem.getCseq(), citems));

				subItems.add(subCItem);
			}
		}

		return subItems;
	}

	public CItemDto getMyEntryPoint(CItemSearchDto citemSearch)
	{
		List<CItemDto> citems = getMyCitemHierarchy(citemSearch);

		if (CollectionUtils.isNotEmpty(citems))
		{
			for (CItemDto citem : citems)
			{
				if (CmStringUtils.isNotEmpty(citem.getEntryPoint()))
				{
					return citem;
				}

				citemSearch.setPseq(citem.getCseq());
				CItemDto subItem = getEntryPoint(citemSearch);

				if (ObjectUtils.isNotEmpty(subItem))
				{
					return subItem;
				}
			}
		}

		return null;
	}

	protected CItemDto getEntryPoint(CItemSearchDto citemSearch)
	{
		for (CItemDto citem: contentService.getCitemHierarchy(citemSearch))
		{
			if (CmStringUtils.isNotEmpty(citem.getEntryPoint()))
			{
				return citem;
			}
		}

		return null;
	}
}
