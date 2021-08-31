package org.jwebppy.platform.mgmt.user.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemUserRlDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicySearchDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyType;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserContactInfoDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.service.CredentialsPolicyService;
import org.jwebppy.platform.mgmt.user.service.UserService;
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

import com.ibm.icu.util.TimeZone;

@Controller
@RequestMapping("/platform/mgmt/user")
public class UserController extends UserGeneralController
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private ContentService contentService;

	@Autowired
	private CredentialsPolicyService credentialsPolicyService;

	@Autowired
	private LoginHistoryService loginHistoryService;

	@Autowired
	private UserService userService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute UserSearchDto userSearch)
	{
		PageableList<UserDto> pageableList = null;

		if (CmStringUtils.isEmpty(userSearch.getQuery()) && userSearch.getUSeq() == null)
		{
			pageableList = new PageableList<>();
		}
		else
		{
			pageableList = new PageableList<>(userService.getPageableUsers(userSearch));
		}

		return UserLayoutBuilder.getList(pageableList);
	}

	@GetMapping("/detail/{tabPath}")
	@ResponseBody
	public Object detail(@PathVariable("tabPath") String tabPath, @ModelAttribute("userSearch") UserSearchDto userSearch)
	{
		if ("authority".equals(tabPath))
		{
			CItemSearchDto cItemSearch = new CItemSearchDto();
			cItemSearch.setUSeq(userSearch.getUSeq());

			return UserLayoutBuilder.getAuthority(contentAuthorityService.getMyCItems(cItemSearch));
		}
		else
		{
			if (CmStringUtils.isNotEmpty(userSearch.getUsername()) || userSearch.getUSeq() != null)
			{
				UserDto user = userService.getUser(userSearch);

				if ("contact".equals(tabPath))
				{
					return UserLayoutBuilder.getContactInfo(user.getUserContactInfo());
				}
				else if ("account".equals(tabPath))
				{
					UserAccountDto userAccount = user.getUserAccount();

					CredentialsPolicySearchDto credentialsPolicySearch = new CredentialsPolicySearchDto();
					credentialsPolicySearch.setCpSeq(userAccount.getCpSeq());
					credentialsPolicySearch.setType(CredentialsPolicyType.U);

					return UserLayoutBuilder.getAccountInfo(userAccount, credentialsPolicyService.getDefaultCredentialPolicyIfEmpty(credentialsPolicySearch));
				}
				else if ("login_history".equals(tabPath))
				{
					LoginHistorySearchDto loginHistorySearch = new LoginHistorySearchDto();
					loginHistorySearch.setUsername(userSearch.getUsername());
					loginHistorySearch.setUSeq(userSearch.getUSeq());

					return UserLayoutBuilder.getSimpleLoginHistories(loginHistoryService.getPageableLoginHistories(loginHistorySearch));
				}
				else
				{
					return UserLayoutBuilder.getGeneralInfo(user);
				}
			}
		}

		return EMPTY_RETURN_VALUE;
	}

	@GetMapping("/write/{tabPath}")
	@ResponseBody
	public Object write(@PathVariable("tabPath") String tabPath, @ModelAttribute("userSearch") UserSearchDto userSearch)
	{
		if ("authority".equals(tabPath))
		{
			List<CItemDto> cItems = null;

			if (userSearch.getUSeq() != null)
			{
				CItemSearchDto cItemSearch = new CItemSearchDto();
				cItemSearch.setUSeq(userSearch.getUSeq());

				cItems = contentAuthorityService.getMyCItemHierarchy(cItemSearch);
			}

			return UserLayoutBuilder.getAuthorityForm(cItems);
		}
		else
		{
			UserDto user = new UserDto();

			if (userSearch.getUSeq() != null)
			{
				user = userService.getUser(userSearch);
			}

			if ("contact".equals(tabPath))
			{
				return UserLayoutBuilder.getContactInfoForm(user.getUserContactInfo());
			}
			else if ("account".equals(tabPath))
			{
				CredentialsPolicySearchDto credentialsPolicySearch = new CredentialsPolicySearchDto();
				credentialsPolicySearch.setType(CredentialsPolicyType.U);
				credentialsPolicySearch.setFgUse(PlatformCommonVo.YES);

				return UserLayoutBuilder.getAccountInfoForm(user.getUserAccount(), credentialsPolicyService.getCredentialPolicies(credentialsPolicySearch));
			}
			else
			{
				return UserLayoutBuilder.getGeneralInfoForm(user);
			}
		}
	}

	@PostMapping("/save/{tabPath}")
	@ResponseBody
	public Object save(@PathVariable("tabPath") String tabPath, @ModelAttribute UserDto user, @ModelAttribute UserAccountDto userAccount, @ModelAttribute UserContactInfoDto userContactInfo, @RequestParam(value = "cSeq", required = false) List<Integer> cSeqs)
	{
		if ("account".equals(tabPath))
		{
			if (CmStringUtils.isNotEmpty(userAccount.getPassword()))
			{
				userAccount.setFgPasswordLocked(PlatformCommonVo.YES);
			}

			return userService.saveUserAccount(userAccount);
		}
		else if ("contact".equals(tabPath))
		{
			return userService.saveUserContactInfo(userContactInfo);
		}
		else if ("authority".equals(tabPath))
		{
			CItemUserRlDto cItemUserRlDto = new CItemUserRlDto();
			cItemUserRlDto.setUSeq(user.getUSeq());
			cItemUserRlDto.setCSeqs(cSeqs);

			return contentAuthorityService.save(cItemUserRlDto);
		}
		else
		{
			return userService.saveUser(user);
		}
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("uSeq") List<Integer> uSeqs)
	{
		return userService.delete(uSeqs);
	}

	@PostMapping("/{command}")
	@ResponseBody
	public Object lock(@PathVariable("command") String command, @RequestParam("uSeq") List<Integer> uSeqs)
	{
		if ("lock".equals(command))
		{
			return userService.lockUserAccount(uSeqs, PlatformCommonVo.YES);
		}
		else if ("unlock".equals(command))
		{
			return userService.lockUserAccount(uSeqs, PlatformCommonVo.NO);
		}

		return EMPTY_RETURN_VALUE;
	}

	@GetMapping("/authority")
	@ResponseBody
	public Object authority(@ModelAttribute CItemSearchDto cItemSearch)
	{
		cItemSearch.setFgShowGroup(PlatformCommonVo.YES);

		return UserLayoutBuilder.getAuthority(contentAuthorityService.getMyCItemHierarchy(cItemSearch));
	}

	@GetMapping("/my_authority")
	@ResponseBody
	public Object myAuthority(@ModelAttribute CItemSearchDto cItemSearch, @RequestParam(value = "cSeqs", required = false) List<Integer> cSeqs)
	{
		List<CItemDto> cItems = null;

		if (CollectionUtils.isNotEmpty(cSeqs))
		{
			cItemSearch.setCSeqs(cSeqs);
			cItemSearch.setFgVisible(PlatformCommonVo.YES);

			cItems = contentService.getCItems(cItemSearch);
		}
		else
		{
			cItems = contentAuthorityService.getMyCItems(cItemSearch);
		}

		return UserLayoutBuilder.getAuthorityForm(cItems);
	}

	@GetMapping("/timezone")
	@ResponseBody
	public Object timezone(@ModelAttribute UserSearchDto userSearch)
	{
		String[] ids = TimeZone.getAvailableIDs(userSearch.getCountry());
		List<Map<String, String>> timezones = new LinkedList<>();

		for (String id : ids)
		{
			TimeZone timezone = TimeZone.getTimeZone(id);

			Map<String, String> timezoneMap = new HashMap<>();
			timezoneMap.put("value", id);
			timezoneMap.put("name", id + ", " + timezone.getDisplayName());
			timezoneMap.put("text", id + ", " + timezone.getDisplayName());

			timezones.add(timezoneMap);
		}

		return timezones;
	}

	@GetMapping("/credentials/valid_check")
	@ResponseBody
	public Object validCheck(@ModelAttribute CredentialsPolicySearchDto credentialsPolicySearch)
	{
		String value = CmStringUtils.trimToEmpty(credentialsPolicySearch.getValue());

		if (CredentialsPolicyType.U.equals(credentialsPolicySearch.getType()))
		{
			UserDto user = userService.getUserByUsername(value.toUpperCase());

			if (user != null)
			{
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("TYPE", credentialsPolicySearch.getType());
				resultMap.put("RESULT", 999);
				resultMap.put("MESSAGE", "The username is not available. Please enter another one.");

				return resultMap;
			}
		}

		CredentialsPolicyDto credentialPolicy = credentialsPolicyService.getDefaultCredentialPolicyIfEmpty(credentialsPolicySearch);

		return credentialsPolicyService.checkValid(credentialPolicy, credentialsPolicySearch.getType(), value);
	}

	@PostMapping("/copy")
	@ResponseBody
	public Object copy(@RequestParam Map<String, String> paramMap)
	{
		return userService.createUserByCopy(paramMap);
	}
}
