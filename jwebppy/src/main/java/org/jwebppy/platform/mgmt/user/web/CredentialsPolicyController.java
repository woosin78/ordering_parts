package org.jwebppy.platform.mgmt.user.web;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.user.UserGeneralController;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicySearchDto;
import org.jwebppy.platform.mgmt.user.service.CredentialsPolicyService;
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

@Controller
@RequestMapping("/platform/mgmt/user/credentials/policy")
public class CredentialsPolicyController extends UserGeneralController
{
	@Autowired
	private CredentialsPolicyService credentialPolicyService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute CredentialsPolicySearchDto credentialPolicySearch)
	{
		return CredentialsPolicyLayoutBuilder.getList(new PageableList<>(credentialPolicyService.getPageableCredentialPolicies(credentialPolicySearch)));
	}

	@PostMapping("/modify/{field}")
	@ResponseBody
	public Object modifyStatus(@PathVariable("field") String field, @RequestParam("cpSeq") Integer cpSeq, @RequestParam("value") String value)
	{
		CredentialsPolicyDto credentialsPolicy = credentialPolicyService.getCredentialPolicy(cpSeq);

		if (CmStringUtils.equals("use", field))
		{
			credentialsPolicy.setFgUse(value);
		}
		else if (CmStringUtils.equals("default", field))
		{
			credentialsPolicy.setFgDefault(value);
		}

		return credentialPolicyService.modify(credentialsPolicy);
	}
}
