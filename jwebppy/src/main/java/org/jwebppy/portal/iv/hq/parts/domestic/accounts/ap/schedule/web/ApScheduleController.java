package org.jwebppy.portal.iv.hq.parts.domestic.accounts.ap.schedule.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.accounts.ap.schedule.service.ApScheduleService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsCommonVo.DOMESTIC_REQUEST_PATH + "/accounts/ap/schedule")
public class ApScheduleController extends PartsDomesticGeneralController
{
	@Autowired
	private ApScheduleService apScheduleService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateThisMonth()));
		model.addAttribute("pToDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pToDate"), CmDateFormatUtils.today()));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addDateByKey(new Object[][] {
				{"fromDate", "pFromDate"},
				{"toDate", "pToDate"}
		});

		RfcResponse rfcResponse = apScheduleService.getList(rfcParamMap);

		DataList dataList = rfcResponse.getTable("I_LIST");
		DataMap dataMap = rfcResponse.getStructure("LS_9189");
		Map<String, Object> summaryMap = new HashMap<>();

		if (dataMap.isNotEmpty())
		{
			//DAAMT: 정상
			//B1AMT + KDAMT: 상계
			//APAMT: 연체
			//BNAMT: 반납
			//DAAMT + B1AMT + APAMT + BNAMT: 합계

			summaryMap.put("SUMMARY_1", dataMap.getDecimal("DAAMT"));//정상
			summaryMap.put("SUMMARY_2", dataMap.getDecimal("B1AMT").add(dataMap.getDecimal("KDAMT")));//상계
			summaryMap.put("SUMMARY_3", dataMap.getDecimal("APAMT"));//연체
			summaryMap.put("SUMMARY_4", dataMap.getDecimal("BNAMT"));//반납
			summaryMap.put("SUMMARY_5", dataMap.getDecimal("DAAMT").add(dataMap.getDecimal("B1AMT")).add(dataMap.getDecimal("APAMT")).add(dataMap.getDecimal("BNAMT")));//합계

			reCalculatePriceByCurrency(summaryMap, new String[] { "SUMMARY_1", "SUMMARY_2", "SUMMARY_3", "SUMMARY_4", "SUMMARY_5" }, null, null, -1);

			FormatBuilder.with(summaryMap)
				.decimalFormat(new String[] { "SUMMARY_1", "SUMMARY_2", "SUMMARY_3", "SUMMARY_4", "SUMMARY_5" });
		}

		reCalculatePriceByCurrency(dataList, new String[] { "TOTAL" }, null, null, -100);

		FormatBuilder.with(dataList)
			.decimalFormat(new String[] { "TOTAL" })
			.dateFormat(new String[] { "SALEDT", "BUDAT" });

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("summary", summaryMap);
		resultMap.put("list", dataList);

		return resultMap;
	}
}
