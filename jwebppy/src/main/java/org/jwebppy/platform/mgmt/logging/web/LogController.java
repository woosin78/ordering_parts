package org.jwebppy.platform.mgmt.logging.web;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.logging.LoggingGeneralController;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/log")
public class LogController extends LoggingGeneralController
{
	@Autowired
	private DataAccessLogService dataAccessLogService;

	@RequestMapping("/main")
	public String main(@ModelAttribute(value = "dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch)
	{
		dataAccessLogSearch.setFromDate(LocalDateTime.now().withDayOfMonth(1));

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/logs")
	@ResponseBody
	public Object logs(@ModelAttribute DataAccessLogSearchDto dataAccessLogSearch)
	{
		PageableList<DataAccessLogDto> pageableList = new PageableList<>();
		pageableList.setList(dataAccessLogService.getPageableLogs(dataAccessLogSearch));

		return LogLayoutBuilder.getList(pageableList);
	}

	@GetMapping("/detail")
	public Object detail(@ModelAttribute("dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch)
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/detail/{dalSeq}")
	@ResponseBody
	public Object log(@PathVariable("dalSeq") Long dalSeq)
	{
		return LogLayoutBuilder.getLog(dataAccessLogService.getLog(dalSeq));
	}
}
