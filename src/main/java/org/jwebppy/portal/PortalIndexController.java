package org.jwebppy.portal;

import java.net.UnknownHostException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.common.web.PlatformGeneralController;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(PortalConfigVo.INDEX_URL)
public class PortalIndexController extends PlatformGeneralController
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@Autowired
	private UserService userService;

	@RequestMapping
	public Object index(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws UnknownHostException
	{
		if (UserAuthenticationUtils.isAuthenticated())
		{
			return new RedirectView(PortalConfigVo.CONTEXT_PATH + "/forward/entry_point");
		}

		return new RedirectView(PortalConfigVo.FORM_LOGIN_PAGE_URL);
	}

	@RequestMapping("/forward/entry_point")
	public Object forwardToEntryPoint(HttpServletRequest request)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setUSeq(getUSeq());

		CItemDto cItem = contentAuthorityService.getMyEntryPoint(cItemSearch);

		if (cItem == null)
		{
			return new ResponseEntity<>("<script>alert('" + i18nMessageSource.getMessage("PLTF_M_NOT_AUTHORIZED") + "'); document.location.href = '" + PortalConfigVo .FORM_LOGOUT_PROCESSING_URL + "';</script>", HttpStatus.UNAUTHORIZED);
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
