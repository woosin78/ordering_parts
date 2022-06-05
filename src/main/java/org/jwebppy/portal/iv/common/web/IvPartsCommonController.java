package org.jwebppy.portal.iv.common.web;

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

import com.google.common.collect.ImmutableMap;

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

		ImmutableMap<String, String> rfcParamMap = new ImmutableMap.Builder<String, String>()
				.put("partNo", pPartNo)
				.put("partDesc", pPartNo)
				.put("salesOrg", erpDataMap.getSalesOrg())
				.put("distChannel", erpDataMap.getDistChannel())
				.put("division", erpDataMap.getDivision())
				.put("lang", erpDataMap.getLangForSap())
				.build();

		return ivPartsCommonService.getPartsInfo(rfcParamMap);
	}

	@GetMapping("/search/dealer/data")
	@ResponseBody
	public Object targetData(@RequestParam("pName") String pName, @RequestParam("pCode") String pCode)
	{
		ErpDataMap erpDataMap = getErpUserInfoByUsername();

		ImmutableMap<String, String> rfcParamMap = new ImmutableMap.Builder<String, String>()
				.put("name", pName)
				.put("dealerCode", pCode)
				.put("salesOrg", erpDataMap.getSalesOrg())
				.put("distChannel", erpDataMap.getDistChannel())
				.put("division", erpDataMap.getDivision())
				.build();

		return ivPartsCommonService.getDealers(rfcParamMap);
	}
}
