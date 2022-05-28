package org.jwebppy.platform.core.security.authentication.service;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;
import org.jwebppy.platform.core.util.CmReflectionUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
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

	protected void setValue(Object target, String fieldName, Object value)
	{
		if (target != null)
		{
			Field field = CmReflectionUtils.findField(target.getClass(), fieldName);

			if (field != null && CmStringUtils.isNotEmpty(value))
			{
				CmReflectionUtils.makeAccessible(field);
				field.setAccessible(true);
				CmReflectionUtils.setField(field, target, value);
			}
		}
	}

	public Authentication getAuthentication(UserDto user)
	{
		UserAccountDto userAccount = user.getUserAccount();
		UserGroupDto userGroup = user.getUserGroup();

		String username = userAccount.getUsername();

        CItemSearchDto userSearch = new CItemSearchDto();
        userSearch.setUSeq(user.getUSeq());
        userSearch.setLang(user.getLanguage());
        userSearch.setTypes(new CItemType[] {CItemType.R, CItemType.G});

        List<CItemDto> cItems = contentAuthorityService.getMyCItems(userSearch);

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
        platformUserDetails.setDateFormat1(userGroup.getDateFormat1());
        platformUserDetails.setTimeFormat1(userGroup.getTimeFormat1());
        platformUserDetails.setDateFormat2(userGroup.getDateFormat2());
        platformUserDetails.setTimeFormat2(userGroup.getTimeFormat2());
        platformUserDetails.setTimezone(user.getTimezone());
        platformUserDetails.setCurrencyFormat(userGroup.getCurrencyFormat());
        platformUserDetails.setWeightFormat(userGroup.getWeightFormat());
        platformUserDetails.setQtyFormat(userGroup.getQtyFormat());
        platformUserDetails.setCItems(cItems);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userAccount.getUsername(), userAccount.getPassword(), platformUserDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(platformUserDetails);

        return usernamePasswordAuthenticationToken;
	}

	public Authentication getAuthentication(Integer uSeq)
	{
		if (CmStringUtils.isNotEmpty(uSeq))
		{
			UserDto user = userService.getUser(uSeq);

			if (ObjectUtils.isNotEmpty(user))
			{
				return getAuthentication(user);
			}
		}

		return null;
	}
}
