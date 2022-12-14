package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/user/login/history")
public class LoginHistoryController extends UserGeneralController
{
	@Autowired
	private LoginHistoryService loginHistoryService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute LoginHistorySearchDto loginHistorySearch)
	{
		return LoginHistoryLayoutBuilder.pageableList(new PageableList<>(loginHistoryService.getPageableLoginHistories(loginHistorySearch)));
	}

	@GetMapping("/last_login_info")
	@ResponseBody
	public Object lastLoginInfo(@ModelAttribute("loginHistorySearchDto") LoginHistorySearchDto loginHistorySearch)
	{
		loginHistorySearch.setRowPerPage(2);
		loginHistorySearch.setUseq(UserAuthenticationUtils.getUserDetails().getUseq());
		loginHistorySearch.setFgResult(MgmtCommonVo.YES);

		List<LoginHistoryDto> loginHistories = loginHistoryService.getPageableLoginHistories(loginHistorySearch);

		if (CollectionUtils.isNotEmpty(loginHistories))
		{
			if (loginHistories.size() > 1)
			{
				return loginHistories.get(1);
			}
			else
			{
				return loginHistories.get(0);
			}
		}

		return null;
	}
}
