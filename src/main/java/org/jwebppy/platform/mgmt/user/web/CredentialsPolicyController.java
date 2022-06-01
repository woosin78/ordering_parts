package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicySearchDto;
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
import org.thymeleaf.util.ListUtils;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/user/credentials/policy")
public class CredentialsPolicyController extends UserGeneralController
{
	@Autowired
	private CredentialsPolicyService credentialsPolicyService;

	@Autowired
	private UserGroupService userGroupService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute CredentialsPolicySearchDto credentialPolicySearch)
	{
		return CredentialsPolicyLayoutBuilder.pageableList(new PageableList<>(credentialsPolicyService.getPageableCredentialsPolicies(credentialPolicySearch)));
	}

	@GetMapping("/write")
	public String write(Model model, WebRequest webRequest, @RequestParam(value = "cpSeq", required = false) Integer cpSeq)
	{
		CredentialsPolicySearchDto credentialsPolicySearch = new CredentialsPolicySearchDto();
		credentialsPolicySearch.setFgDefault(PlatformCommonVo.YES);

		Boolean isShowFgDefault = (ListUtils.isEmpty(credentialsPolicyService.getCredentialsPolicies(credentialsPolicySearch))) ? true : false;

		CredentialsPolicyDto credentialsPolicy = new CredentialsPolicyDto();

		if (ObjectUtils.isNotEmpty(cpSeq))
		{
			credentialsPolicy = credentialsPolicyService.getCredentialPolicy(cpSeq);

			if (CmStringUtils.equals(credentialsPolicy.getFgDefault(), PlatformCommonVo.YES))
			{
				isShowFgDefault = true;
			}
		}

		model.addAttribute("credentialsPolicy", credentialsPolicy);
		model.addAttribute("userGroups", userGroupService.getUserGroups(null));
		model.addAttribute("isShowFgDefault", isShowFgDefault);

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute CredentialsPolicyDto credentialsPolicy, WebRequest webRequest)
	{
		credentialsPolicy.setName(CmStringUtils.upperCase(credentialsPolicy.getName()));

		return credentialsPolicyService.save(credentialsPolicy);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("cpSeq") List<Integer> cpSeqs)
	{
		return credentialsPolicyService.delete(cpSeqs);
	}

	@GetMapping("/check/valid_name")
	@ResponseBody
	public Object checkValidName(@RequestParam(value = "cpSeq", required = false) Integer cpSeq, @RequestParam(value = "name") String name)
	{
		CredentialsPolicyDto credentialsPolicy = credentialsPolicyService.getCredentialPolicyByName(CmStringUtils.upperCase(name));

		if (ObjectUtils.isEmpty(credentialsPolicy))
		{
			return PlatformCommonVo.SUCCESS;
		}

		if (ObjectUtils.isNotEmpty(cpSeq))
		{
			if (cpSeq.equals(credentialsPolicy.getCpSeq()))
			{
				return PlatformCommonVo.SUCCESS;
			}
		}

		return PlatformCommonVo.FAIL;
	}
}
