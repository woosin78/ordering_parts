package org.jwebppy.portal.iv.mgmt.account.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.mgmt.account.dto.AccountDto;
import org.jwebppy.portal.iv.mgmt.account.dto.UserType;
import org.jwebppy.portal.iv.mgmt.account.service.AccountMgmtService;
import org.jwebppy.portal.iv.mgmt.account.utils.AccountMgmtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

		String[] saleaArea = AccountMgmtUtils.getSalesArea(AccountMgmtUtils.getBizTypeBySalesArea(userInfoMap));

		Map<String, Object> salesAreaMap = new HashMap<>();
		salesAreaMap.put("VKORG", saleaArea[0]);
		salesAreaMap.put("VTWEG", saleaArea[1]);

		/*
		Map<String, Object> salesAreaMap = new ImmutableMap.Builder<String, Object>()
				.put("VKORG", saleaArea[0])
				.put("VTWEG", saleaArea[1])
				.build();
				*/

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

			if (UserType.D.equals(account.getUserType()))
			{
				account.setPassword(IvCommonVo.INITIAL_PASSWORD);
			}

			return account;
		}

		return result;
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam(value="uSeqs") List<Integer> uSeqs)
	{
		userService.deleteUser(uSeqs);

		return IvCommonVo.SUCCESS;
	}

	@PostMapping("/reset/password")
	@ResponseBody
	public Object resetPassword(@RequestParam(value="uSeqs") List<Integer> uSeqs)
	{
		userService.resetPassword(uSeqs);

		return IvCommonVo.INITIAL_PASSWORD;
	}
}
