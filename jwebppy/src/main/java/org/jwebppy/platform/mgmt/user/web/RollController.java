package org.jwebppy.platform.mgmt.user.web;

import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/roll")
public class RollController extends UserGeneralController
{
	@Autowired
	private ContentService contentService;

	@RequestMapping("/main")
	public String main()
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/rolls")
	@ResponseBody
	public Object rolls(@ModelAttribute CItemSearchDto cItemSearch)
	{
		return RollLayoutBuilder.getList(new PageableList<>(contentService.getPageableCItems(cItemSearch)));
	}
}
