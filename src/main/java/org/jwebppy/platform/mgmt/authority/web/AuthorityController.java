package org.jwebppy.platform.mgmt.authority.web;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.authority.dto.CItemAuthRlDto;
import org.jwebppy.platform.mgmt.authority.service.AuthorityService;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/authority")
public class AuthorityController extends UserGeneralController
{
	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ContentService contentService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("type", webRequest.getParameter("type"));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute CItemSearchDto citemSearch)
	{
		PageableList<CItemDto> pageableList = null;

		if (ObjectUtils.allNull(citemSearch.getType(), citemSearch.getCseq()) && CmStringUtils.isEmpty(citemSearch.getQuery()))
		{
			pageableList = new PageableList<>();
		}
		else
		{
			citemSearch.setTypes(new CItemType[] {CItemType.R, CItemType.G});

			pageableList = new PageableList<>(contentService.getPageableCitems(citemSearch));
		}

		return AuthorityLayoutBuilder.pageableList(pageableList);
	}

	@GetMapping("/view/layout/{tabPath}")
	@ResponseBody
	public Object viewLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute("citemSearch") CItemSearchDto citemSearch, @ModelAttribute("userSearch") UserSearchDto userSearch)
	{
		if ("authority".equals(tabPath))
		{
			CItemDto citem = contentService.getCitem(citemSearch.getCseq());

			if (citem.getType().equals(CItemType.G))
			{
				 return AuthorityLayoutBuilder.viewAuthority(authorityService.getCitemAuthorities(citemSearch));
			}

			return Collections.emptyList();
		}

		return AuthorityLayoutBuilder.viewGeneralInfo(contentService.getCitem(citemSearch.getCseq()));
	}

	@GetMapping("/write/layout/{tabPath}")
	@ResponseBody
	public Object writeLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute("citemSearch") CItemSearchDto citemSearch)
	{
		if ("authority".equals(tabPath))
		{
			List<CItemDto> citems = null;

			if (citemSearch.getCseq() != null)
			{
				citems = authorityService.getSubRoles(CItemSearchDto.builder()
						.pseq(citemSearch.getCseq())
						.build());
			}

			return AuthorityLayoutBuilder.writeAuthority(citems);
		}
		else
		{
			CItemDto citem = new CItemDto();

			if (citemSearch.getCseq() != null)
			{
				citem = contentService.getCitem(citemSearch.getCseq());
			}

			return AuthorityLayoutBuilder.writeGeneralInfo(citem);
		}
	}

	@PostMapping("/save/{tabPath}")
	@ResponseBody
	public Object save(@PathVariable("tabPath") String tabPath, @ModelAttribute CItemDto citem, @RequestParam(value = "cseq", required = false) List<Integer> cseqs)
	{
		if ("authority".equals(tabPath))
		{
			CItemAuthRlDto citemAuthRl = new CItemAuthRlDto();
			citemAuthRl.setPseq(citem.getCseq());
			citemAuthRl.setCseqs(cseqs);

			return authorityService.save(citemAuthRl);
		}
		else
		{
			citem.setName(CmStringUtils.upperCase(citem.getName()));

			if (citem.getCseq() != null)
			{
				CItemDto citem2 = contentService.getCitem(citem.getCseq());

				citem2.setName(citem.getName());
				citem2.setDescription(citem.getDescription());
				citem2.setFromValid(citem.getFromValid());
				citem2.setToValid(citem.getToValid());
				citem2.setFgVisible(citem.getFgVisible());
				citem2.setSort(citem.getSort());

				return contentService.modify(citem2);
			}
			else
			{
				citem.setType(CItemType.G);

				return contentService.create(citem);
			}
		}
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("cseq") List<Integer> cseqs)
	{
		return contentService.delete(cseqs);
	}

	@GetMapping("/sub_role/layout")
	@ResponseBody
	public Object subRoleLayout(@ModelAttribute CItemSearchDto citemSearch, @RequestParam(value = "cseqs", required = false) List<Integer> cseqs)
	{
		List<CItemDto> citems = null;

		if (CollectionUtils.isNotEmpty(cseqs))
		{
			citemSearch.setCseqs(cseqs);
			citemSearch.setFgVisible(MgmtCommonVo.YES);

			citems = contentService.getCitems(citemSearch);
		}
		else
		{
			citemSearch.setPseq(citemSearch.getCseq());

			citems = authorityService.getSubRoles(citemSearch);

		}

		return AuthorityLayoutBuilder.writeAuthority(citems);
	}
}
