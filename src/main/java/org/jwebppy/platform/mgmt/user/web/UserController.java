package org.jwebppy.platform.mgmt.user.web;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.cache.CacheClear;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
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
import org.jwebppy.platform.mgmt.user.dto.UserPasswordChangeHistoryDto;
import org.jwebppy.platform.mgmt.user.dto.UserSearchDto;
import org.jwebppy.platform.mgmt.user.service.CredentialsPolicyService;
import org.jwebppy.platform.mgmt.user.service.UserGroupService;
import org.jwebppy.platform.mgmt.user.service.UserPasswordChangeHistoryService;
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
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/user")
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
	private UserPasswordChangeHistoryService userPasswordChangeHistoryService;

	@Autowired
	private UserService userService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		CItemSearchDto citemSearch = CItemSearchDto.builder()
				.fgVisible(MgmtCommonVo.YES)
				.types(new CItemType[] {CItemType.R, CItemType.G})
				.build();

		model.addAttribute("citems", contentService.getCitems(citemSearch));
		model.addAttribute("userGroups", userGroupService.getUserGroups(null));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute UserSearchDto userSearch)
	{
		return UserLayoutBuilder.pageableList(new PageableList<>(userService.getPageableUsers(userSearch)));
	}

	@GetMapping("/view/layout/{tabPath}")
	@ResponseBody
	public Object viewLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute UserSearchDto userSearch)
	{
		if ("authority".equals(tabPath))
		{
			return UserLayoutBuilder.viewAuthority(contentAuthorityService.getMyCitems(userSearch.getUseq()));
		}
		else
		{
			if (CmStringUtils.isNotEmpty(userSearch.getUsername()) || ObjectUtils.isNotEmpty(userSearch.getUseq()))
			{
				UserDto user = userService.getUser(userSearch);

				if ("contact".equals(tabPath))
				{
					return UserLayoutBuilder.viewContactInfo(user.getUserContactInfo());
				}
				else if ("account".equals(tabPath))
				{
					UserAccountDto userAccount = user.getUserAccount();

					CredentialsPolicyDto credentialPolicy = userAccount.getCredentialsPolicy();

					if (ObjectUtils.isNotEmpty(credentialPolicy))
					{
						credentialPolicy = credentialsPolicyService.getDefaultCredentialPolicyIfEmpty(CredentialsPolicySearchDto.builder()
								.cpSeq(credentialPolicy.getCpSeq())
								.type(CredentialsPolicyType.U)
								.build());
					}

					return UserLayoutBuilder.viewAccountInfo(userAccount, credentialPolicy);
				}
				else if ("login_history".equals(tabPath))
				{
					return LoginHistoryLayoutBuilder.listLoginHistory(loginHistoryService.getPageableLoginHistories(LoginHistorySearchDto.builder()
							.username(user.getUserAccount().getUsername())
							.pageNumber(1)
							.rowPerPage(20)
							.build()));
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
	public Object writeLayout(@PathVariable("tabPath") String tabPath, @ModelAttribute UserSearchDto userSearch)
	{
		if ("authority".equals(tabPath))
		{
			List<CItemDto> citems = null;

			if (ObjectUtils.isNotEmpty(userSearch.getUseq()))
			{
				citems = contentAuthorityService.getMyCitemHierarchy(CItemSearchDto.builder()
						.useq(userSearch.getUseq())
						.build());
			}

			return UserLayoutBuilder.writeAuthority(citems);
		}
		else
		{
			UserDto user = new UserDto();

			if (ObjectUtils.isNotEmpty(userSearch.getUseq()))
			{
				user = userService.getUser(userSearch);
			}

			if ("contact".equals(tabPath))
			{
				return UserLayoutBuilder.writeContactInfo(user.getUserContactInfo(), user.getUserGroup());
			}
			else if ("account".equals(tabPath))
			{
				if (user.getUserAccount().isEmpty())
				{
					user.setUserGroup(userGroupService.getUserGroup(user.getUserGroup().getUgSeq()));
				}

				return UserLayoutBuilder.writeAccountInfo(user, credentialsPolicyService.getCredentialsPolicies(CredentialsPolicySearchDto.builder()
						.fgUse(MgmtCommonVo.YES)
						.build()));
			}
			else
			{
				return UserLayoutBuilder.writeGeneralInfo(user, userGroupService.getUserGroups(null));
			}
		}
	}

	@PostMapping("/save/{tabPath}")
	@CacheClear(name = "A")
	@ResponseBody
	public Object save(@PathVariable("tabPath") String tabPath,
			@ModelAttribute UserDto user, @ModelAttribute UserAccountDto userAccount, @ModelAttribute UserContactInfoDto userContactInfo,
			@ModelAttribute CItemUserRlDto citemUserRl, @ModelAttribute UserGroupDto userGroup,  @ModelAttribute CredentialsPolicyDto credentialsPolicy, WebRequest webRequest)
	{
		if ("account".equals(tabPath))
		{
			if (CmStringUtils.isNotEmpty(userAccount.getPassword()))
			{
				userAccount.setFgPasswordLocked(MgmtCommonVo.YES);
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
			String[] cseqs = webRequest.getParameterValues("cseq");

			if (cseqs != null && cseqs.length > 0)
			{
				citemUserRl.setCseqs(Arrays.asList(cseqs)
						.stream()
						.map(s -> Integer.parseInt(s))
						.collect(Collectors.toList()));
			}

			return contentAuthorityService.save(citemUserRl);
		}
		else
		{
			user.setUserGroup(userGroup);

			return userService.saveUser(user);
		}
	}

	@PostMapping("/delete")
	@CacheClear(name = "A")
	@ResponseBody
	public Object delete(@RequestParam("useq") List<Integer> useqs)
	{
		return userService.deleteUser(useqs);
	}

	@PostMapping("/{command}")
	@CacheClear(name = "A")
	@ResponseBody
	public Object lock(@PathVariable("command") String command, @RequestParam("useq") List<Integer> useqs)
	{
		if ("lock".equals(command))
		{
			return userService.lockUserAccount(useqs, MgmtCommonVo.YES);
		}
		else if ("unlock".equals(command))
		{
			return userService.lockUserAccount(useqs, MgmtCommonVo.NO);
		}

		return EMPTY_RETURN_VALUE;
	}

	@GetMapping("/layout/authority")
	@ResponseBody
	public Object authorityLayout(@ModelAttribute CItemSearchDto citemSearch)
	{
		citemSearch.setFgShowGroup(MgmtCommonVo.YES);

		return UserLayoutBuilder.viewAuthority(contentAuthorityService.getMyCitemHierarchy(citemSearch));
	}

	@GetMapping("/my_authority")
	@ResponseBody
	public Object myAuthority(@ModelAttribute CItemSearchDto citemSearch, @RequestParam(value = "cseqs", required = false) List<Integer> cseqs)
	{
		List<CItemDto> citems = null;

		if (CollectionUtils.isNotEmpty(cseqs))
		{
			citemSearch.setCseqs(cseqs);
			citemSearch.setFgVisible(MgmtCommonVo.YES);

			citems = contentService.getCitems(citemSearch);
		}
		else
		{
			citems = contentAuthorityService.getMyCitems(citemSearch);
		}

		return UserLayoutBuilder.writeAuthority(citems);
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

	@PostMapping("/copy")
	@CacheClear(name = "A")
	@ResponseBody
	public Object copy(@RequestParam Map<String, String> paramMap)
	{
		return userService.createUserByCopy(paramMap);
	}

	@GetMapping("/check/expired_password")
	@ResponseBody
	public Object checkPasswordExpiration()
	{
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("expiredDate", "");
		resultMap.put("difference", "-1");

		UserDto user = userService.getUser(UserAuthenticationUtils.getUseq());
		UserAccountDto userAccount = user.getUserAccount();

		if (CmStringUtils.equals(userAccount.getFgNoUsePassword(), MgmtCommonVo.YES))
		{
			resultMap.put("difference", "999");
		}
		else
		{
			int pwdValidPeriod = NumberUtils.toInt(userAccount.getCredentialsPolicy().getPvalidPeriod(), 0);

			if (pwdValidPeriod > 0)
			{
				List<UserPasswordChangeHistoryDto> userPasswordChangeHistories = userPasswordChangeHistoryService.getPageableUserPasswordChangeHistories(UserPasswordChangeHistoryDto.builder()
						.useq(getUseq())
						.pageNumber(1)
						.rowPerPage(1)
						.build());

				LocalDateTime regDate = null;

				if (CollectionUtils.isNotEmpty(userPasswordChangeHistories))
				{
					UserPasswordChangeHistoryDto userPasswordChangeHistory = userPasswordChangeHistories.get(0);

					regDate = userPasswordChangeHistory.getRegDate();
				}
				else
				{
					regDate = user.getRegDate();
				}

				String timezone = user.getTimezone();
				ZonedDateTime zonedRegDate = CmDateTimeUtils.toZonedDateTime(regDate, timezone);
				ZonedDateTime exiredDate = zonedRegDate.plusDays(pwdValidPeriod);

				resultMap.put("expiredDate", CmDateFormatUtils.format(exiredDate, CmDateFormatUtils.getDateFormat()));
				resultMap.put("difference", Long.toString(ChronoUnit.DAYS.between(CmDateTimeUtils.now(timezone), exiredDate)));
			}
		}

		return resultMap;
	}
}
