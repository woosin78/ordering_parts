package org.jwebppy.platform.mgmt.logging.web;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.logging.LoggingGeneralController;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/log")
public class LogController extends LoggingGeneralController
{
	@Autowired
	private DataAccessLogService dataAccessLogService;

	@RequestMapping("/list")
	public String list(@ModelAttribute(value = "dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch)
	{
		dataAccessLogSearch.setFromDate(LocalDateTime.now().withDayOfMonth(1));

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute DataAccessLogSearchDto dataAccessLogSearch)
	{
		return LogLayoutBuilder.getList(new PageableList<>(dataAccessLogService.getPageableLogs(dataAccessLogSearch)));
	}

	@GetMapping("/detail_popup")
	public Object detailPopup(@ModelAttribute("dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch)
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/detail_popup/data")
	@ResponseBody
	public Object log(@RequestParam("dlSeq") Long dlSeq)
	{
		return LogLayoutBuilder.getLog(dataAccessLogService.getLog(dlSeq));
	}
}
