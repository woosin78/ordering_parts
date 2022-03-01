package org.jwebppy.portal.iv.hq.parts.export.accounts.ap.schedule.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.platform.core.util.Formatter;
import org.jwebppy.portal.iv.hq.parts.export.accounts.ap.schedule.service.ExApScheduleService;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.web.PartsExportGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/accounts/ap/schedule")
public class ExApScheduleController extends PartsExportGeneralController
{
	@Autowired
	private ExApScheduleService apScheduleService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.plusMonths(-6)));
		model.addAttribute("pToDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pToDate"), CmDateFormatUtils.today()));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		RfcResponse rfcResponse = apScheduleService.getList(getErpUserInfo());

		DataList dataList = rfcResponse.getTable("T_RECEIPT");

		if (CollectionUtils.isNotEmpty(dataList))
		{
			double[] amount = {0.0, 0.0, 0.0};

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				amount[0] += dataMap.getDouble("WRBTR");
				amount[1] += dataMap.getDouble("WRBTR3");
				amount[2] += dataMap.getDouble("WRSHB");
			}

			DataMap amountMap = new DataMap();
			amountMap.put("FAEDT", "");
			amountMap.put("WRBTR", amount[0]);
			amountMap.put("WRBTR3", amount[1]);
			amountMap.put("WRSHB", amount[2]);

			dataList.add(0, amountMap);

			FormatBuilder.with(dataList)
				.decimalFormat(new String[] {"WRBTR", "WRBTR3", "WRSHB"})
				.dateFormat("FAEDT");

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("apList", dataList);
			resultMap.put("totalAmount", Formatter.fixedPoint(Formatter.getDefDecimalFormat(rfcResponse.getString("O_WRSHB"))));

			return resultMap;
		}

		return EMPTY_RETURN_VALUE;
	}
}
