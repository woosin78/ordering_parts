package org.jwebppy.portal.iv.hq.parts.export.report.order.machine.web;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

	public static void main(String[] args)
	{
		String dt = "2022.05.01";

		//CmDateTimeUtils.toLocalDateTime(dt, "yyyy.MM.ddHHmmdd");

		System.err.println(CmDateFormatUtils.defaultZonedFormat(CmDateTimeUtils.parse(dt), "yyyy"));

	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		String pFromYear = CmStringUtils.trimToEmpty(paramMap.get("pFromDate"));
		String pToDate = CmStringUtils.trimToEmpty(paramMap.get("pToDate"));

		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap
			.addDate(new Object[][] {
				{"fromDate", pFromYear},
				{"toDate", pToDate}
			});

		DataList dataList = machineOrderReportService.getList(rfcParamMap).getTable("T_LIST");

		Map<String, Map<Integer, Integer>> resultMap = new HashMap<>();
		int fromYear = Integer.parseInt(CmDateFormatUtils.defaultZonedFormat(CmDateTimeUtils.parse(pFromYear), "yyyy"));
		int toYear = Integer.parseInt(CmDateFormatUtils.defaultZonedFormat(CmDateTimeUtils.parse(pToDate), "yyyy"));

		System.err.println("fromYear:" + fromYear);
		System.err.println("fromYear:" + toYear);

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			DataMap dataMap = (DataMap)dataList.get(i);

			String model = dataMap.getString("MDLNM");
			Map<Integer, Integer> countMap;

			if (resultMap.containsKey(model))
			{
				countMap = resultMap.get(model);
			}
			else
			{
				countMap = new TreeMap<>();

				//for (int j=toYear; j>=fromYear; j--)
				for (int j=fromYear; j<=toYear; j++)
				{
					countMap.put(j, 0);
				}

				resultMap.put(model, countMap);
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

		System.err.println(resultMap);

		return resultMap;
	}
}
