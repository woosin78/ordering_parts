package org.jwebppy.platform.mgmt.authority.web;

import java.util.Collections;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/authority")
public class AuthorityController extends UserGeneralController
{
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
			cItemSearch.setTypes(new String[] {PlatformCommonVo.GROUP, PlatformCommonVo.ROLE});

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
			CItemDto cItem = contentService.getCItem(userSearch.getCSeq());

			if (CmStringUtils.equals(PlatformCommonVo.GROUP, cItem.getType()))
			{
				 return AuthorityLayoutBuilder.getAuthority(contentService.findCItemAuthorities(cItemSearch));
			}

			return Collections.emptyList();
		}
		else if ("user".equals(tabPath))
		{
			return AuthorityLayoutBuilder.getUsers(userService.getUsersInCItem(userSearch));
		}

		return AuthorityLayoutBuilder.getGeneralInfo(contentService.getCItem(cItemSearch.getCSeq()));
	}
}
