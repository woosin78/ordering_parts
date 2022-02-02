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
import org.jwebppy.portal.iv.mgmt.account.service.AccountMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listDate(@RequestParam Map<String, Object> paramMap)
	{
		if (CmStringUtils.isEmpty(paramMap.get("pUsername")) && CmStringUtils.isEmpty(paramMap.get("pDealerCode")))
		{
			return EMPTY_RETURN_VALUE;
		}

		String bizType = CmStringUtils.trimToEmpty(paramMap.get("bizType"));

		String[] saleaArea = getSalesArea(bizType);

		Map<String, Object> salesAreaMap = new HashMap<>();
		salesAreaMap.put("VKORG", saleaArea[0]);
		salesAreaMap.put("VTWEG", saleaArea[1]);
		salesAreaMap.put("WERKS", saleaArea[2]);

		List<Map<String, Object>> salesAreaList = new ArrayList<>();
		salesAreaList.add(salesAreaMap);

		ErpDataMap rfcParamMap = getErpUserInfo(null, null)
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
	public String write()
	{
		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = new ErpDataMap(paramMap);

		int result = accountMgmtService.save(rfcParamMap);

		if (result > 0)
		{
			UserDto user = userService.getUser(result);

			Map<String, Object> returnMap = new HashMap<>();
			returnMap.put("uSeq", user.getUSeq());
			returnMap.put("username", user.getUserAccount().getUsername());

			if (rfcParamMap.isEquals("userType", "D"))
			{
				returnMap.put("password", IvCommonVo.INITIAL_PASSWORD);
			}

			return returnMap;
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

	protected String[] getSalesArea(String bizType)
	{
		Map<String, String[]> salesAreaMap = new HashMap<>();

		salesAreaMap.put("EUDOP", new String[] { "7816", "10", "60" });//EUDOP
		salesAreaMap.put("UKDOP", new String[] { "7216", "10", "60" });//UKDOP
		salesAreaMap.put("HQDOP", new String[] { "F116", "10", "60" });//HQDOP
		salesAreaMap.put("HQEXP", new String[] { "F116", "20", "60" });//HQEXP

		return salesAreaMap.get(bizType);
	}
}
