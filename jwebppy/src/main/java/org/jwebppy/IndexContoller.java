package org.jwebppy;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.exception.security.NoAuthorityException;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping
public class IndexContoller extends PlatformGeneralController
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private UserService userService;

	@RequestMapping("/")
	public Object index(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
	{
		if (UserAuthenticationUtils.isLogin())
		{
			return new RedirectView("/forward/entry_point");
		}

		return new RedirectView(PlatformConfigVo.FORM_LOGIN_PAGE_URL);
	}

	@RequestMapping("/forward/entry_point")
	public Object forwardToEntryPoint(HttpServletRequest request)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setUSeq(getUSeq());

		CItemDto cItem = contentAuthorityService.getMyEntryPoint(cItemSearch);

		if (cItem == null)
		{
			throw new NoAuthorityException("There is no authority in your account.");
		}

		UserDto user = userService.getUserByUsername(getUsername());

		String url = cItem.getUrl();

		if (CmStringUtils.isEmpty(cItem.getParameter()))
		{
			url += "?";
		}
		else
		{
			url += "&";
		}

		url += "lang=" + CmStringUtils.defaultIfEmpty(user.getLanguage(), Locale.ENGLISH);

		return new RedirectView(url);
	}
}
