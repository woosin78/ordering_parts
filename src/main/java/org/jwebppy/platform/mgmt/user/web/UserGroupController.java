package org.jwebppy.platform.mgmt.user.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.cache.CacheClear;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.common.web.MgmtGeneralController;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.conn_resource.service.SapConnResourceService;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicySearchDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupSearchDto;
import org.jwebppy.platform.mgmt.user.service.CredentialsPolicyService;
import org.jwebppy.platform.mgmt.user.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/user/user_group")
public class UserGroupController extends MgmtGeneralController
{
	@Autowired
	private CredentialsPolicyService credentialsPolicyService;

	@Autowired
	private SapConnResourceService sapConnResourceService;

	@Autowired
	private UserGroupService userGroupService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute UserGroupSearchDto userGroupSearch)
	{
		return UserGroupLayoutBuilder.pageableList(new PageableList<>(userGroupService.getPageableUserGroups(userGroupSearch)));
	}

	@GetMapping("/view")
	public Object view(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/view/layout")
	@ResponseBody
	public Object viewLayout(@RequestParam("ugSeq") Integer ugSeq)
	{
		return UserGroupLayoutBuilder.view(userGroupService.getUserGroup(ugSeq));
	}

	@GetMapping("/write")
	public Object write(Model model, WebRequest webRequest, @RequestParam(required = false, name="ugSeq") Integer ugSeq)
	{
		UserGroupDto userGroup = (ugSeq == null) ? new UserGroupDto() : userGroupService.getUserGroup(ugSeq);

		model.addAttribute("userGroup", userGroup);
		model.addAttribute("mode", webRequest.getParameter("mode"));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/write/layout")
	@ResponseBody
	public Object writeLayout(@ModelAttribute UserGroupDto userGroup, @RequestParam(required = false, name="mode") String mode)
	{
		Integer ugSeq = userGroup.getUgSeq();

		if (ugSeq != null)
		{
			userGroup = userGroupService.getUserGroup(ugSeq);
		}

		CredentialsPolicySearchDto credentialsPolicySearch = new CredentialsPolicySearchDto();
		credentialsPolicySearch.setFgUse(MgmtCommonVo.YES);

		return UserGroupLayoutBuilder.write(userGroup, sapConnResourceService.getAvailableSapConnResources(), credentialsPolicyService.getCredentialsPolicies(credentialsPolicySearch), mode);
	}

	@PostMapping("/save")
	@CacheClear(name = "A")
	@ResponseBody
	public Object save(@ModelAttribute UserGroupDto userGroup, @ModelAttribute SapConnResourceDto sapConnResource, @ModelAttribute CredentialsPolicyDto credentialsPolicy, @RequestParam List<String> langKind)
	{
		userGroup.setName(CmStringUtils.upperCase(userGroup.getName()));
		userGroup.setSapConnResource(sapConnResource);
		userGroup.setCredentialsPolicy(credentialsPolicy);
		userGroup.setLangKind(CmStringUtils.arrayToString(langKind));

		return userGroupService.save(userGroup);
	}

	@PostMapping("/delete")
	@CacheClear(name = "A")
	@ResponseBody
	public Object delete(@RequestParam("ugSeq") List<Integer> ugSeqs)
	{
		return userGroupService.delete(ugSeqs);
	}

	@GetMapping("/lang_kind")
	@ResponseBody
	public Object langKind(@ModelAttribute UserGroupSearchDto userGroupSearch)
	{
		UserGroupDto userGroup = userGroupService.getUserGroup(userGroupSearch.getUgSeq());

		List<Map<String, String>> langKinds = new ArrayList<>();

		for (String language: CmStringUtils.split(userGroup.getLangKind(), PlatformConfigVo.DELIMITER))
		{
			Map<String, String> resultMap = new HashMap<>();

			resultMap.put("name", new Locale(language).getDisplayLanguage());
			resultMap.put("value", language);

			langKinds.add(resultMap);
		}

		return langKinds;
	}

	@GetMapping("/check/valid_name")
	@ResponseBody
	public Object checkValidName(@RequestParam(value = "ugSeq", required = false) Integer ugSeq, @RequestParam(value = "name") String name)
	{
		UserGroupDto userGroup = userGroupService.getUserGroupByName(CmStringUtils.upperCase(name));

		if (ObjectUtils.isEmpty(userGroup))
		{
			return MgmtCommonVo.SUCCESS;
		}

		if (ObjectUtils.isNotEmpty(ugSeq))
		{
			if (ugSeq.equals(userGroup.getUgSeq()))
			{
				return MgmtCommonVo.SUCCESS;
			}
		}

		return MgmtCommonVo.FAIL;
	}
}
