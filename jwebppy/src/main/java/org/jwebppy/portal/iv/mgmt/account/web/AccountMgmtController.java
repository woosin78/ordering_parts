package org.jwebppy.portal.iv.mgmt.account.web;

import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.mgmt.account.service.AccountMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH + "/mgmt/account")
public class AccountMgmtController extends IvGeneralController
{
	@Autowired
	private AccountMgmtService accountMgmtService;

	@Autowired
	private UserService userService;

	@RequestMapping("/list")
	public String accountList()
	{
		return DEFAULT_VIEW_URL;
	}

	/*
	@RequestMapping("/list/data")
	@ResponseBody
	public Object dealerListData(@RequestParam Map<String, Object> paramMap)
	{
		if (CmStringUtils.isEmpty(paramMap.get("pUsername")) && CmStringUtils.isEmpty(paramMap.get("pDealerCode")))
		{
			return EMPTY_RETURN_VALUE;
		}

		ErpUserContext erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();

		Map<String, Object> salesAreaMap = new HashMap<>();
		salesAreaMap.put("VKORG", erpUserContext.getSalesOrg());
		salesAreaMap.put("VTWEG", erpUserContext.getDistChl());
		salesAreaMap.put("WERKS", erpUserContext.getDivision());

		List<Map<String, Object>> salesAreaList = new ArrayList<>();
		salesAreaList.add(salesAreaMap);

		ErpDataMap rfcParamMap = getErpUserInfo()
				.with(paramMap)
				.addByKey(new Object[][] {
					{"dealerCode", "pDealerCode"},
					{"username", "pUsername"}
				})
				.add("salesAreaList", salesAreaList);

		DataList dataList = accountMgmtService.getMappingList(rfcParamMap);

		List<DataMap> accountList = new ArrayList<>();

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			DataMap dataMap = (DataMap)dataList.get(i);

			UserDto user = userService.getUserByUsername(dataMap.getString("BNAME"));

			if (user == null || user.getUserAccount() == null)
			{
				continue;
			}

			String email = null;

			if (user.getUserContactInfo() != null)
			{
				email = user.getUserContactInfo().getEmail();
			}

			dataMap.put("U_SEQ", user.getUSeq());
			dataMap.put("LASTNAME", user.getLastName());
			dataMap.put("FIRSTNAME", user.getFirstName());
			dataMap.put("EMAIL", email);

			accountList.add(dataMap);
		}

		return accountList;
	}

	@RequestMapping("/write")
	public String write()
	{
		return DEFAULT_VIEW_URL;
	}

	/*
	@PostMapping("/save")
	@ResponseBody
	public Object save(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.putAll(paramMap);

		int result = accountService.save(rfcParamMap);

		if (result == 1)
		{
			String type = rfcParamMap.getString("type");

			if ("I".equals(type))
			{
				return type;
			}

			return EuCommonVo.INITIAL_PASSWORD;
		}

		return result;
	}

	@PostMapping("/reset_password")
	@ResponseBody
	public Object resetPassword(@RequestParam(value="uSeqs") List<Integer> uSeqs)
	{
		if (accountService.resetPassword(uSeqs) > 0)
		{
			return EuCommonVo.INITIAL_PASSWORD;
		}

		return EMPTY_RETURN_VALUE;
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value="uSeqs") List<Integer> uSeqs)
	{
		userService.deleteUser(uSeqs);

		return EMPTY_RETURN_VALUE;
	}
	*/
}
