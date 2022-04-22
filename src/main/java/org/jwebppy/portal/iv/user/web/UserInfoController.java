package org.jwebppy.portal.iv.user.web;

import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/user")
public class UserInfoController extends IvGeneralController
{
	@Autowired
	private UserService userService;

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest)
	{
		UserDto user = userService.getUser(getUSeq());

		model.addAttribute("langKinds", user.getUserGroup().getLangKind2());
		model.addAttribute("myLang", user.getLanguage());

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/save")
	@ResponseBody
	public Object save(@RequestParam UserDto pUser)
	{
		UserDto user = userService.getUser(getUSeq());
		user.setLanguage(pUser.getLanguage());

		userService.saveUser(user);

		return EMPTY_RETURN_VALUE;
	}
}
