package org.jwebppy.portal.iv.hq.parts.domestic.claim.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.domestic.claim.service.ClaimDisplayService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/claim/display")
public class ClaimDisplayController extends PartsDomesticGeneralController
{
	@Autowired
	private ClaimDisplayService claimDisplayService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("pClaimNo", CmStringUtils.trimToEmpty(webRequest.getParameter("pClaimNo")));
		model.addAttribute("pFromDate", CmStringUtils.defaultIfEmpty(webRequest.getParameter("pFromDate"), CmDateFormatUtils.theFirstDateMonth(CmDateTimeUtils.now())));
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
			.addByKey(new Object[][] {
				{"referenceNo", "pReferenceNo"},
				{"claimNo", "pClaimNo"},
				{"partNo", "pPartNo"}
			})
			.addDateByKey(new Object[][] {
				{"fromDate", "pFromDate"},
				{"toDate", "pToDate"}
			});

		RfcResponse rfcResponse = claimDisplayService.getList(rfcParamMap);
		DataList dataList = rfcResponse.getTable("LT_SEARCH2");

		FormatBuilder.with(dataList)
			.dateFormat("ERDAT")
			.decimalFormat("T_NETWR");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("claimReason1List", claimDisplayService.getClaimReasonList(rfcParamMap));
		resultMap.put("claimList", dataList);

		return resultMap;
	}

	@RequestMapping("/view")
	public Object view(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object viewData(@RequestParam Map<String, Object> paramMap)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"orderNo", "pOrderNo"},
				{"docType", "pDocType"}
			});

		RfcResponse rfcResponse = claimDisplayService.getView(rfcParamMap);

		DataList itemList = rfcResponse.getTable("LT_ITEM");

		FormatBuilder.with(itemList)
			.qtyFormat("QTY")
			.dateFormat("ZZDEALDT");

		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("itemList", itemList);
		resultMap.put("fileList", rfcResponse.getTable("LT_FILE"));

		rfcParamMap.add("reason1", "");
		resultMap.put("reason1List", claimDisplayService.getClaimReasonList(rfcParamMap));

		rfcParamMap.add("reason1", "X");
		resultMap.put("reason2List", claimDisplayService.getClaimReasonList(rfcParamMap));

		return resultMap;
	}

	@RequestMapping("/download/doc")
	public void downloadDocument(@RequestParam Map<String, Object> paramMap, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"orderNo", "orderNo"},
				{"docType", "docType"}
			});

		RfcResponse rfcResponse = claimDisplayService.getView(rfcParamMap);

		downloadByRfc(httpServletRequest, httpServletResponse, rfcResponse.getTable("LT_FILE2"), "FILE_DATA", "Complain_" + CmStringUtils.trimToEmpty(paramMap.get("orderNo")) + ".pdf");
	}

	@RequestMapping("/download/attachment")
	public void downloadAttachment(@RequestParam Map<String, Object> paramMap, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
	{
		PartsErpDataMap rfcParamMap = getErpUserInfo();

		rfcParamMap.with(paramMap)
			.addByKey(new Object[][] {
				{"fileName", "fileName"},
				{"orderNo", "orderNo"},
				{"intNo", "intNo"},
				{"docuItem", "docuItem"}
			});

		RfcResponse rfcResponse = claimDisplayService.getAttachment(rfcParamMap);

		downloadByRfc(httpServletRequest, httpServletResponse, rfcResponse.getTable("LT_FILE"), "FILE_DATA", CmStringUtils.trimToEmpty(paramMap.get("fileName")));
	}
}
