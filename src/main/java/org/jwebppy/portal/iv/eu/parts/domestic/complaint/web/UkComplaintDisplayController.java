package org.jwebppy.portal.iv.eu.parts.domestic.complaint.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.FormatBuilder;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.common.web.EuPartsDomesticGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.complaint.service.UkComplaintDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/corp/uk/scm/parts/complaint/display")
@PreAuthorize("!hasRole('ROLE_EU_SS_READ-ONLY_DEALER')")
public class UkComplaintDisplayController extends EuPartsDomesticGeneralController
{
	@Autowired
	private UkComplaintDisplayService complaintDisplayService;

	@RequestMapping("/complaint_list")
	public String complaintDisplayList(Model model, WebRequest webRequest)
	{
		String sDate = CmDateFormatUtils.today().substring(0,7)+"-01";
		if (CmStringUtils.isEmpty(webRequest.getParameter("pFromDate")))
		{
			model.addAttribute("pFromDate", sDate);
		}

		if (CmStringUtils.isEmpty(webRequest.getParameter("pToDate")))
		{
			model.addAttribute("pToDate", CmDateFormatUtils.today());
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/complaint_list/data")
	@ResponseBody
	public Object complaintDisplayData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("referenceNo", paramMap.get("pReferenceNo"));	//ReferenceNo
		rfcParamMap.put("complaintNo", paramMap.get("pComplaintNo"));		//ComplaintNo
		rfcParamMap.put("partNo", paramMap.get("pPartNo"));	//Part No.
		rfcParamMap.put("fromDate", CmStringUtils.trimToEmpty(paramMap.get("pFromDate")).replaceAll("-", ""));
		rfcParamMap.put("toDate", CmStringUtils.trimToEmpty(paramMap.get("pToDate")).replaceAll("-", ""));

		RfcResponse rfcResponse = null;
		rfcResponse = complaintDisplayService.getList(rfcParamMap);
		DataList resultOrderList = rfcResponse.getTable("LT_SEARCH2");

		rfcResponse = complaintDisplayService.getComplaintReasonList(rfcParamMap);
		DataList reasonList1 = rfcResponse.getTable("LT_VALUESET");

		FormatBuilder.with(resultOrderList)
			.dateFormat("ERDAT", EuCommonVo.DEFAULT_DATE_FORMAT)
			.decimalFormat("T_NETWR", "#,##0.00");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("reasonList1", reasonList1);
		resultMap.put("resultOrderList", resultOrderList);

		return resultMap;
	}

	@RequestMapping("/complaint_detail")
	public Object complaintDetail(@RequestParam Map<String, Object> paramMap, Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/complaint_detail/data")
	@ResponseBody
	public Object complaintDetailData(@RequestParam Map<String, Object> paramMap)
	{
		ErpDataMap rfcParamMap = getErpUserInfo();
		rfcParamMap.put("orderNo", paramMap.get("pOrderNo"));
		rfcParamMap.put("docType", paramMap.get("pDocType"));

		RfcResponse rfcResponse = complaintDisplayService.getDetail(rfcParamMap);
		DataList itemList = rfcResponse.getTable("LT_ITEM");
		DataList fileList = rfcResponse.getTable("LT_FILE");

		rfcParamMap.put("reason1", "");
		rfcResponse = complaintDisplayService.getComplaintReasonList(rfcParamMap);
		DataList reasonList1 = rfcResponse.getTable("LT_VALUESET");

		rfcParamMap.put("reason1", "X");
		rfcResponse = complaintDisplayService.getComplaintReasonList(rfcParamMap);
		DataList reasonList2 = rfcResponse.getTable("LT_VALUESET");

		FormatBuilder.with(itemList)
		.decimalFormat("QTY", "#,##0")
		.dateFormat("ZZDEALDT", EuCommonVo.DEFAULT_DATE_FORMAT);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("detailList", itemList);
		resultMap.put("fileList", fileList);
		resultMap.put("reasonList1", reasonList1);
		resultMap.put("reasonList2", reasonList2);

		return resultMap;
	}
}