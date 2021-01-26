package org.jwebppy.platform.mgmt.authority.web;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.authority.dto.CItemAuthRlDto;
import org.jwebppy.platform.mgmt.authority.service.AuthorityService;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/authority")
public class AuthorityController extends UserGeneralController
{
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private UserService userService;

	@RequestMapping("/main")
	public String main()
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/authorities")
	@ResponseBody
	public Object authorities(@ModelAttribute CItemSearchDto cItemSearch)
	{
		PageableList<CItemDto> pageableList = null;

		if (CmStringUtils.isEmpty(cItemSearch.getQuery()))
		{
			pageableList = new PageableList<>();
		}
		else
		{
			//cItemSearch.setTypes(new String[] {PlatformCommonVo.GROUP, PlatformCommonVo.ROLE});
			cItemSearch.setTypes(new String[] {CItemType.R.name(), CItemType.G.name()});

			pageableList = new PageableList<>(contentService.getPageableCItems(cItemSearch));
		}

		return AuthorityLayoutBuilder.getList(pageableList);
	}

	@GetMapping("/{tabPath}")
	@ResponseBody
	public Object view(@PathVariable("tabPath") String tabPath, @ModelAttribute("cItemSearch") CItemSearchDto cItemSearch, @ModelAttribute("userSearch") UserSearchDto userSearch)
	{
		if ("authority".equals(tabPath))
		{
			CItemDto cItem = contentService.getCItem(cItemSearch.getCSeq());

			if (cItem.getType().equals(CItemType.G))
			{
				 return AuthorityLayoutBuilder.getAuthority(authorityService.getCItemAuthorities(cItemSearch));
			}

			return Collections.emptyList();
		}
		else if ("user".equals(tabPath))
		{
			return AuthorityLayoutBuilder.getUsers(userService.getUsersInCItem(userSearch));
		}

		return AuthorityLayoutBuilder.getGeneralInfo(contentService.getCItem(cItemSearch.getCSeq()));
	}

	@GetMapping({"/modify/{tabPath}", "/create/{tabPath}"})
	@ResponseBody
	public Object modify(@PathVariable("tabPath") String tabPath, @ModelAttribute("cItemSearch") CItemSearchDto cItemSearch)
	{
		if ("authority".equals(tabPath))
		{
			List<CItemDto> cItems = null;

			if (cItemSearch.getCSeq() != null)
			{
				CItemSearchDto cItemSearch2 = new CItemSearchDto();
				cItemSearch2.setPSeq(cItemSearch.getCSeq());

				cItems = authorityService.getSubRoles(cItemSearch2);
			}

			return AuthorityLayoutBuilder.getAuthorityForm(cItems);
		}
		else
		{
			CItemDto cItem = new CItemDto();

			if (cItemSearch.getCSeq() != null)
			{
				cItem = contentService.getCItem(cItemSearch.getCSeq());
			}

			return AuthorityLayoutBuilder.getGeneralInfoForm(cItem);
		}
	}

	@PostMapping("/save/{tabPath}")
	@ResponseBody
	public Object save(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemDto cItem, @RequestParam(value = "cSeq", required = false) List<Integer> cSeqs)
	{
		if ("authority".equals(tabPath))
		{
			CItemAuthRlDto cItemAuthRl = new CItemAuthRlDto();
			cItemAuthRl.setPSeq(cItem.getCSeq());
			cItemAuthRl.setCSeqs(cSeqs);

			return authorityService.save(cItemAuthRl);
		}
		else
		{
			if (cItem.getCSeq() != null)
			{
				CItemDto cItem2 = contentService.getCItem(cItem.getCSeq());
				cItem2.setName(cItem.getName());
				cItem2.setDescription(cItem.getDescription());
				cItem2.setFromValid(cItem.getFromValid());
				cItem2.setToValid(cItem.getToValid());

				return contentService.modify(cItem2);
			}
			else
			{
				cItem.setType(CItemType.G);

				return contentService.create(cItem);
			}
		}
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("cSeq") List<Integer> cSeqs)
	{
		return contentService.delete(cSeqs);
	}

	@GetMapping("/sub_roles")
	@ResponseBody
	public Object subRoles(@ModelAttribute CItemSearchDto cItemSearch, @RequestParam(value = "cSeqs", required = false) List<Integer> cSeqs)
	{
		List<CItemDto> cItems = null;

		if (CollectionUtils.isNotEmpty(cSeqs))
		{
			cItemSearch.setCSeqs(cSeqs);
			cItemSearch.setFgVisible(PlatformCommonVo.YES);

			cItems = contentService.getCItems(cItemSearch);
		}
		else
		{
			cItemSearch.setPSeq(cItemSearch.getCSeq());

			cItems = authorityService.getSubRoles(cItemSearch);

		}

		return AuthorityLayoutBuilder.getAuthorityForm(cItems);
	}
}
