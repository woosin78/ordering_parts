package org.jwebppy.portal.iv.common.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.service.IvPartsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH + "/parts")
public class IvPartsCommonController extends IvGeneralController
{
	@Autowired
	private IvPartsCommonService ivPartsCommonService;

	@RequestMapping("/search/data")
	@ResponseBody
	public Object searchData(@RequestParam Map<String, Object> paramMap)
	{
		String pPartNo = CmStringUtils.upperCase(CmStringUtils.trimToEmpty(paramMap.get("pPartNo")));

		if ("".equals(pPartNo))
		{
			return EMPTY_RETURN_VALUE;
		}

		ErpDataMap erpDataMap = getErpUserInfoByUsername();

		Map<String, String> rfcParamMap = new HashMap<>();
		rfcParamMap.put("partNo", pPartNo);
		rfcParamMap.put("partDesc", pPartNo);
		rfcParamMap.put("salesOrg", erpDataMap.getSalesOrg());
		rfcParamMap.put("distChannel", erpDataMap.getDistChannel());
		rfcParamMap.put("division", erpDataMap.getDivision());
		rfcParamMap.put("lang", erpDataMap.getLangForSap());

		return ivPartsCommonService.getPartsInfo(rfcParamMap);
	}

	@GetMapping("/search/dealer/data")
	@ResponseBody
	public Object targetData(@RequestParam("pName") String pName, @RequestParam("pCode") String pCode)
	{
		ErpDataMap erpDataMap = getErpUserInfoByUsername();

		Map<String, String> rfcParamMap = new HashMap<>();
		rfcParamMap.put("name", pName);
		rfcParamMap.put("dealerCode", pCode);
		rfcParamMap.put("salesOrg", erpDataMap.getSalesOrg());
		rfcParamMap.put("distChannel", erpDataMap.getDistChannel());
		rfcParamMap.put("division", erpDataMap.getDivision());

		return ivPartsCommonService.getDealers(rfcParamMap);
	}
}
