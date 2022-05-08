package org.jwebppy.portal.iv.hq.parts.export.report.order.machine.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.jwebppy.portal.iv.hq.parts.export.common.web.PartsExportGeneralController;
import org.jwebppy.portal.iv.hq.parts.export.report.order.machine.service.ExMachineOrderReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.google.common.collect.ImmutableMap;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH + "/report/order/machine")
public class ExMachineOrderReportController extends PartsExportGeneralController
{
	@Autowired
	private ExMachineOrderReportService machineOrderReportService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth()));
		model.addAttribute("pToDate", CmDateFormatUtils.today());

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		String pFromDate = CmStringUtils.trimToEmpty(paramMap.get("pFromDate"));
		String pToDate = CmStringUtils.trimToEmpty(paramMap.get("pToDate"));

		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap
			.addDate(new Object[][] {
				{"fromDate", pFromDate},
				{"toDate", pToDate}
			});

		return format(machineOrderReportService.getList(rfcParamMap).getTable("T_LIST"), pFromDate, pToDate);
	}

	private Map<String, Object> format(DataList dataList, String fromDate, String toDate)
	{
		int fromYear = Integer.parseInt(CmDateFormatUtils.defaultZonedFormat(CmDateTimeUtils.parse(fromDate), "yyyy"));
		int toYear = Integer.parseInt(CmDateFormatUtils.defaultZonedFormat(CmDateTimeUtils.parse(toDate), "yyyy"));

		Map<String, Map<Integer, Integer>> reportMap = new LinkedHashMap<>();

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			DataMap dataMap = (DataMap)dataList.get(i);

			String model = dataMap.getString("MDLNM");
			Map<Integer, Integer> countMap;

			if (reportMap.containsKey(model))
			{
				countMap = reportMap.get(model);
			}
			else
			{
				countMap = new LinkedHashMap<>();

				//for (int j=toYear; j>=fromYear; j--)
				for (int j=fromYear; j<=toYear; j++)
				{
					countMap.put(j, 0);
				}

				reportMap.put(model, countMap);
			}

			int year = dataMap.getInt("YEAR");

			if (countMap.containsKey(year))
			{
				countMap.put(year, countMap.get(year) + dataMap.getInt("FKIMG"));
			}
			else
			{
				countMap.put(year, dataMap.getInt("FKIMG"));
			}
		}

		ImmutableMap<String, Object> resultMap = new ImmutableMap.Builder<String, Object>()
				.put("from", fromYear)
				.put("to", toYear)
				.put("models", ListUtils.emptyIfNull(new ArrayList<>(reportMap.keySet())))
				.put("values", ListUtils.emptyIfNull(new ArrayList<>(reportMap.values())))
				.build();

		return resultMap;
	}
}
