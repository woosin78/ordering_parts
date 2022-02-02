package org.jwebppy.portal.iv.hq.parts.common.web;

import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PartsCommonVo.REQUEST_PATH)
public class DealerInfoController extends PartsGeneralController
{
//	@Resource
//	private DealerInfoService dealerInfoService;
//
//	@RequestMapping("/detail")
//	public Object dealerInfo(Model model, WebRequest webRequest)
//	{
//		addAllAttributeFromRequest(model, webRequest);
//
//		return DEFAULT_VIEW_URL;
//	}
//
//	@RequestMapping("/detail/data")
//	@ResponseBody
//	public Object dealerInfoData(@RequestParam Map<String, Object> paramMap)
//	{
//		return dealerInfoService.getDealerInfo(getErpUserInfo()).getObject("O_DATA");
//	}
}
