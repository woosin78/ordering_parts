package org.jwebppy.platform.security.authentication.service;

import java.util.List;

import org.jwebppy.platform.PlatformGeneralService;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.platform.security.authentication.dto.PlatformUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService extends PlatformGeneralService
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private UserService userService;

	public Authentication getAuthentication(UserDto user)
	{
		UserAccountDto userAccount = user.getUserAccount();

		String username = userAccount.getUsername();

        CItemSearchDto userSearch = new CItemSearchDto();
        userSearch.setUSeq(user.getUSeq());
        userSearch.setLang(user.getLanguage());

        List<CItemDto> cItems = contentAuthorityService.getMyItemHierarchy(userSearch);

        PlatformUserDetails platformUserDetails = new PlatformUserDetails();
        platformUserDetails.setUSeq(user.getUSeq());
        platformUserDetails.setName(user.getName());
        platformUserDetails.setUsername(username);
        platformUserDetails.setPassword(userAccount.getPassword());
        platformUserDetails.setFgAccountLocked(userAccount.getFgAccountLocked());
        platformUserDetails.setFgPasswordLocked(userAccount.getFgPasswordLocked());
        platformUserDetails.setFromValid(userAccount.getFromValid());
        platformUserDetails.setToValid(userAccount.getToValid());
        platformUserDetails.setLanguage(user.getLanguage());
        platformUserDetails.setCItems(cItems);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userAccount.getUsername(), userAccount.getPassword(), platformUserDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(platformUserDetails);

        return usernamePasswordAuthenticationToken;
	}

	public Authentication getAuthentication(String username)
	{
		UserDto user = userService.getUserByUsername(username);

		if (user != null)
		{
			return getAuthentication(user);
		}

		return null;
	}
}
