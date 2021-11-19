package org.jwebppy.portal.iv.hq.parts.common.web;

import java.util.Map;

import org.jwebppy.portal.iv.hq.parts.common.service.DealerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/scm/parts/common/info/dealer")
public class DealerInfoController extends PartsGeneralController
{
	@Autowired
	private DealerInfoService dealerInfoService;

	@RequestMapping("/detail")
	public Object dealerInfo(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/detail/data")
	@ResponseBody
	public Object dealerInfoData(@RequestParam Map<String, Object> paramMap)
	{
		return dealerInfoService.getDealerInfo(getErpUserInfo()).getObject("O_DATA");
	}
}
