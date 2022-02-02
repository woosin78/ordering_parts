package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

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
		return CredentialsPolicyLayoutBuilder.pageableList(new PageableList<>(credentialsPolicyService.getPageableCredentialPolicies(credentialPolicySearch)));
	}

	@GetMapping("/write")
	public String write(Model model, WebRequest webRequest)
	{
		CredentialsPolicyDto credentialsPolicy = new CredentialsPolicyDto();

		String cpSeq = webRequest.getParameter("cpSeq");

		if (CmStringUtils.isNotEmpty(cpSeq))
		{
			credentialsPolicy = credentialsPolicyService.getCredentialPolicy(Integer.valueOf(cpSeq));
		}

		model.addAttribute("credentialsPolicy", credentialsPolicy);
		model.addAttribute("userGroups", userGroupService.getUserGroups(null));

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute CredentialsPolicyDto inCredentialsPolicy, WebRequest webRequest)
	{
		return credentialsPolicyService.save(inCredentialsPolicy);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("cpSeq") List<Integer> cpSeqs)
	{
		return credentialsPolicyService.delete(cpSeqs);
	}
}
