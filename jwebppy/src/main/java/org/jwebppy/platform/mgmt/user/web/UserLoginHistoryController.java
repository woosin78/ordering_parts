package org.jwebppy.platform.mgmt.user.web;

import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.jwebppy.platform.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.security.authentication.service.LoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/user/login/history")
public class UserLoginHistoryController extends UserGeneralController
{
	@Autowired
	private LoginHistoryService loginHistoryService;

	@RequestMapping("/list")
	public String list()
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute("loginHistorySearchDto") LoginHistorySearchDto loginHistorySearch)
	{
		return UserLayoutBuilder.getLoginHistories(new PageableList<>(loginHistoryService.getPageableLoginHistories(loginHistorySearch)));
	}
}
