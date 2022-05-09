package org.jwebppy.portal.iv.mgmt.mapping.web;

import java.util.Map;

import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.mgmt.mapping.service.DealerMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH + "/mgmt/mapping")
@PreAuthorize("hasAnyRole('ROLE_DP_ACCOUNTS_MANAGER', 'ROLE_DP_IVDO_PARTS_MANAGER', 'ROLE_DP_IVEX_PARTS_MANAGER')")
public class DealerMappingController extends IvGeneralController
{
	@Autowired
	private DealerMappingService dealerMappingService;

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
		if (CmStringUtils.isEmpty(paramMap.get("pName")))
		{
			return EMPTY_RETURN_VALUE;
		}

		ErpDataMap rfcParamMap = getErpUserInfo(null, null);
		rfcParamMap.add("name", paramMap.get("pName"));

		return dealerMappingService.getList(rfcParamMap).getTable("LT_DEALER");
	}

	@PostMapping("/change")
	@ResponseBody
	public Object change(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo(null, null);
		rfcParamMap.add("fromUsername", paramMap.get("fromUsername"));

		return dealerMappingService.changeMapping(rfcParamMap).getStructure("O_RETURN");
	}
}
