package org.jwebppy.platform.mgmt.user.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
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
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.service.CredentialsPolicyService;
import org.jwebppy.platform.mgmt.user.service.UserGroupService;
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
import org.yaml.snakeyaml.util.ArrayUtils;

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
	private UserGroupService userGroupService;

	@Autowired
	private UserService userService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute UserSearchDto userSearch)
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

		return UserLayoutBuilder.pageableList(pageableList);
	}

	@GetMapping("/view/layout/{tabPath}")
	@ResponseBody
	public Object viewLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute("userSearch") UserSearchDto userSearch)
	{
		if ("authority".equals(tabPath))
		{
			return UserLayoutBuilder.viewAuthority(contentAuthorityService.getMyCItems(userSearch.getUSeq()));
		}
		else
		{
			if (CmStringUtils.isNotEmpty(userSearch.getUsername()) || userSearch.getUSeq() != null)
			{
				UserDto user = userService.getUser(userSearch);

				if ("contact".equals(tabPath))
				{
					return UserLayoutBuilder.viewContactInfo(user.getUserContactInfo());
				}
				else if ("account".equals(tabPath))
				{
					UserAccountDto userAccount = user.getUserAccount();

					CredentialsPolicySearchDto credentialsPolicySearch = new CredentialsPolicySearchDto();
					credentialsPolicySearch.setCpSeq(userAccount.getCredentialsPolicy().getCpSeq());
					credentialsPolicySearch.setType(CredentialsPolicyType.U);

					return UserLayoutBuilder.viewAccountInfo(userAccount, credentialsPolicyService.getDefaultCredentialPolicyIfEmpty(credentialsPolicySearch));
				}
				else if ("login_history".equals(tabPath))
				{
					LoginHistorySearchDto loginHistorySearch = new LoginHistorySearchDto();
					loginHistorySearch.setUsername(userSearch.getUsername());
					loginHistorySearch.setPageNumber(1);
					loginHistorySearch.setRowPerPage(20);

					return LoginHistoryLayoutBuilder.listLoginHistory(loginHistoryService.getPageableLoginHistories(loginHistorySearch));
				}
				else
				{
					return UserLayoutBuilder.viewGeneralInfo(user);
				}
			}
		}

		return EMPTY_RETURN_VALUE;
	}

	@GetMapping("/write/layout/{tabPath}")
	@ResponseBody
	public Object writeLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute("userSearch") UserSearchDto userSearch)
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

			return UserLayoutBuilder.writeAuthority(cItems);
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
				return UserLayoutBuilder.writeContactInfo(user.getUserContactInfo());
			}
			else if ("account".equals(tabPath))
			{
				CredentialsPolicySearchDto credentialsPolicySearch = new CredentialsPolicySearchDto();
				credentialsPolicySearch.setFgUse(PlatformCommonVo.YES);

				return UserLayoutBuilder.writeAccountInfo(user.getUserAccount(), credentialsPolicyService.getCredentialPolicies(credentialsPolicySearch));
			}
			else
			{
				return UserLayoutBuilder.writeGeneralInfo(user, userGroupService.getUserGroups(null));
			}
		}
	}

	@PostMapping("/save/{tabPath}")
	@ResponseBody
	public Object save(@PathVariable("tabPath") String tabPath,
			@ModelAttribute UserDto user, @ModelAttribute UserAccountDto userAccount, @ModelAttribute UserContactInfoDto userContactInfo,
			@ModelAttribute CItemUserRlDto cItemUserRl, @ModelAttribute UserGroupDto userGroup,  @ModelAttribute CredentialsPolicyDto credentialsPolicy, WebRequest webRequest)
	{
		if ("account".equals(tabPath))
		{
			if (CmStringUtils.isNotEmpty(userAccount.getPassword()))
			{
				userAccount.setFgPasswordLocked(PlatformCommonVo.YES);
			}

			userAccount.setCredentialsPolicy(credentialsPolicy);

			return userService.saveUserAccount(userAccount);
		}
		else if ("contact".equals(tabPath))
		{
			return userService.saveUserContactInfo(userContactInfo);
		}
		else if ("authority".equals(tabPath))
		{
			/*
			List<Integer> cSeqs = CmModelMapperUtils.mapAll(ListUtils.toList(webRequest.getParameterValues("cSeq")), Integer.class);

			CItemUserRlDto cItemUserRl = new CItemUserRlDto();
			cItemUserRl.setUSeq(user.getUSeq());
			cItemUserRl.setCSeqs(cSeqs);
			*/

			cItemUserRl.setCSeqs(CmModelMapperUtils.mapAll(ArrayUtils.toUnmodifiableList(webRequest.getParameterValues("cSeq")), Integer.class));

			return contentAuthorityService.save(cItemUserRl);
		}
		else
		{
			user.setUserGroup(userGroup);

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

	@GetMapping("/layout/authority")
	@ResponseBody
	public Object authorityLayout(@ModelAttribute CItemSearchDto cItemSearch)
	{
		cItemSearch.setFgShowGroup(PlatformCommonVo.YES);

		return UserLayoutBuilder.viewAuthority(contentAuthorityService.getMyCItemHierarchy(cItemSearch));
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

		return UserLayoutBuilder.writeAuthority(cItems);
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
