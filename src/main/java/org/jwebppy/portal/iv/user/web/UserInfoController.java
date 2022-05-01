package org.jwebppy.portal.iv.user.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.common.service.ChangePasswordService;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/user")
public class UserInfoController extends IvGeneralController
{
	@Autowired
	private ChangePasswordService changePasswordService;

	@Autowired
	private UserService userService;

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest)
	{
		UserDto user = userService.getUser(getUSeq());

		model.addAttribute("langKinds", user.getUserGroup().getLangKind2());
		model.addAttribute("myLang", user.getLanguage());

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/save")
	@ResponseBody
	public Object save(HttpSession httpSession, WebRequest webRequest)
	{
		String username = CmStringUtils.defaultIfEmpty(httpSession.getAttribute(PlatformConfigVo.FORM_LOGIN_USERNAME), UserAuthenticationUtils.getUsername());
		String language = CmStringUtils.trimToEmpty(webRequest.getParameter("language"));
		String password = CmStringUtils.trimToEmpty(webRequest.getParameter("password"));
		String newPassword = CmStringUtils.trimToEmpty(webRequest.getParameter("newPassword"));
		String confirmPassword = CmStringUtils.trimToEmpty(webRequest.getParameter("confirmPassword"));

		Map<String, Object> resultMap = new HashMap<>();

		UserDto user = userService.getUser(getUSeq());
		user.setLanguage(language);

		resultMap.put("RESULT", userService.saveUser(user));

		if (!CmStringUtils.isAnyEmpty(password, newPassword, confirmPassword) && CmStringUtils.equals(newPassword, confirmPassword))
		{
			resultMap.putAll(changePasswordService.checkValidPassword(username, password, newPassword, confirmPassword));
		}

		return resultMap;
	}
}
