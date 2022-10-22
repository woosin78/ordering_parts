package org.jwebppy.portal.iv.mgmt.account.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyType;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.service.CredentialsPolicyService;
import org.jwebppy.platform.mgmt.user.service.UserGroupService;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.mgmt.account.dto.AccountDto;
import org.jwebppy.portal.iv.mgmt.account.dto.UserType;
import org.jwebppy.portal.iv.mgmt.account.service.AccountMgmtService;
import org.jwebppy.portal.iv.mgmt.account.utils.AccountMgmtUtils;
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
@RequestMapping(IvCommonVo.REQUEST_PATH + "/mgmt/account")
public class AccountMgmtController extends IvGeneralController
{
	@Autowired
	private AccountMgmtService accountMgmtService;

	@Autowired
	private CredentialsPolicyService credentialsPolicyService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private UserService userService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		if (CmStringUtils.isEmpty(paramMap.get("pUsername")) && CmStringUtils.isEmpty(paramMap.get("pDealerCode")))
		{
			return EMPTY_RETURN_VALUE;
		}

		ErpDataMap userInfoMap = getErpUserInfoByUsername();

		String[] saleaArea = AccountMgmtUtils.getSalesArea(AccountMgmtUtils.getBizTypeByMyAuthority());

		Map<String, Object> salesAreaMap = new HashMap<>();
		salesAreaMap.put("VKORG", saleaArea[0]);
		salesAreaMap.put("VTWEG", saleaArea[1]);

		List<Map<String, Object>> salesAreaList = new ArrayList<>();
		salesAreaList.add(salesAreaMap);

		userInfoMap = getErpUserInfoByUsername().with(paramMap)
				.addByKey(new Object[][] {
					{"dealerCode", "pDealerCode"},
					{"username", "pUsername"}
				})
				.add("salesAreaList", salesAreaList);

		DataList dataList = accountMgmtService.getMappingList(userInfoMap);

		List<DataMap> accountList = new ArrayList<>();

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			DataMap dataMap = (DataMap)dataList.get(i);

			UserDto user = userService.getUserByUsername(dataMap.getString("BNAME"));

			if (user == null)
			{
				continue;
			}

			dataMap.put("U_SEQ", user.getUSeq());
			dataMap.put("LASTNAME", user.getLastName());
			dataMap.put("FIRSTNAME", user.getFirstName());

			accountList.add(dataMap);
		}

		return accountList;
	}

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest)
	{
		model.addAttribute("bizType", AccountMgmtUtils.getBizTypeBySalesArea(getErpUserInfoByUsername()));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute AccountDto account)
	{
		//Default is mine
		account.setBizType(AccountMgmtUtils.getBizTypeBySalesArea(getErpUserInfoByUsername()));

		//정상적으로 계정이 생성되었을 경우 PLTF_USER 식별자 반환
		int result = accountMgmtService.save(account);

		if (result > 0)
		{
			UserDto user = userService.getUser(result);

			account.setUSeq(user.getUSeq());
			account.setUsername(user.getUserAccount().getUsername());

			if (CmStringUtils.equals(PortalCommonVo.NO, user.getUserAccount().getFgNoUsePassword()))
			{
				account.setPassword(IvCommonVo.INITIAL_PASSWORD);
			}

			return account;
		}

		return result;
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value = "uSeqs") List<Integer> uSeqs)
	{
		userService.deleteUser(uSeqs);

		return IvCommonVo.SUCCESS;
	}

	@PostMapping("/reset/password")
	@ResponseBody
	public Object resetPassword(@RequestParam(value = "uSeqs") List<Integer> uSeqs)
	{
		userService.resetPassword(uSeqs);

		return IvCommonVo.INITIAL_PASSWORD;
	}

	@GetMapping("/check/valid_credentials")
	@ResponseBody
	public Object checkValidCredentials(@ModelAttribute AccountDto account)
	{
		String username = CmStringUtils.upperCase(account.getUsername());

		if (CmStringUtils.isEmpty(username))
		{
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("MESSAGE", i18nMessageSource.getMessage("HQP_M_EMPTY_USERNAME"));

			return resultMap;
		}

		if (userService.isExistByUsername(username))
		{
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("MESSAGE", i18nMessageSource.getMessage("HQP_M_EXIST_USERNAME", new Object[] { username }));

			return resultMap;
		}

		String bizType = AccountMgmtUtils.getBizTypeBySalesArea(getErpUserInfoByUsername());
		UserType userType = account.getUserType();

		//DoobizPlus 시스템은 딜러전용 시스템으로 내부사용자는 딜러가 아니기 때문에 임의의 딜러코들 맵핑시켜 준다.
		if (userType.equals(UserType.I))
		{
			account.setDealerCode(AccountMgmtUtils.getDefaultMappingCode(bizType, userType));
		}

		RfcResponse rfcRespose = accountMgmtService.getDealerGeneralInfo(account.getDealerCode());
		DataMap userInfoMap = (DataMap)rfcRespose.getTable("O_KNA").get(0);

		if (MapUtils.isEmpty(userInfoMap))
		{
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("MESSAGE", i18nMessageSource.getMessage("HQP_M_NOT_EXIST_DEALER", new Object[] { account.getDealerCode() }));

			return resultMap;
		}

		String tzone = CmStringUtils.removeEnd(rfcRespose.getString("O_TZONE"), "00");

		if (CmStringUtils.equals(tzone, "P00"))
		{
			tzone = "PGM";
		}

		UserGroupDto userGroup = userGroupService.getUserGroupByName(AccountMgmtUtils.getUserGroupName(bizType, tzone));

		if (ObjectUtils.isEmpty(userGroup))
		{
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("MESSAGE", i18nMessageSource.getMessage("HQP_M_NOT_FOUND_USER_GROUP"));

			return resultMap;
		}

		if (ObjectUtils.isEmpty(userGroup.getCredentialsPolicy().getCpSeq()))
		{
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("MESSAGE", i18nMessageSource.getMessage("HQP_M_NOT_FOUND_CREDENTIALS_POLICY"));

			return resultMap;
		}

		return credentialsPolicyService.checkValid(userGroup.getCredentialsPolicy(), CredentialsPolicyType.U, username);
	}
}
