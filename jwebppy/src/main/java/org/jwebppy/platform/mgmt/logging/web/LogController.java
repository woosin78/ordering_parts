package org.jwebppy.platform.mgmt.logging.web;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.logging.LoggingGeneralController;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessResultLogDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.jwebppy.platform.mgmt.logging.service.DataAccessResultLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/log")
public class LogController extends LoggingGeneralController
{
	@Autowired
	private DataAccessLogService dataAccessLogService;

	@Autowired
	private DataAccessResultLogService dataAccessResultLogService;

	@RequestMapping("/list")
	public String list(@ModelAttribute(value = "dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch)
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	public Object listLayout(@ModelAttribute DataAccessLogSearchDto dataAccessLogSearch)
	{
		return LogLayoutBuilder.pageableList(new PageableList<>(dataAccessLogService.getPageableLogs(dataAccessLogSearch)));
	}

	@GetMapping("/view")
	public String view(@ModelAttribute("dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch, Model model)
	{
		DataAccessLogDto dataAccessLog = dataAccessLogService.getLog(dataAccessLogSearch.getDlSeq());

		if (CollectionUtils.isNotEmpty(dataAccessResultLogService.getResultLogs(dataAccessLog.getDlSeq())))
		{
			model.addAttribute("fgHasResultLog", PlatformCommonVo.YES);
		}

		model.addAttribute("dataAccessLog", dataAccessLog);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/view/layout/{tabPath}")
	@ResponseBody
	public Object log(@PathVariable("tabPath") String tabPath, @RequestParam("dlSeq") String dlSeq)
	{
		if ("parameter".equals(tabPath))
		{
			return LogLayoutBuilder.view(dataAccessLogService.getLog(dlSeq));
		}
		else if ("result".equals(tabPath))
		{
			List<DataAccessResultLogDto> dataAccessResultLogs = dataAccessResultLogService.getResultLogs(dlSeq);

			if (CollectionUtils.isNotEmpty(dataAccessResultLogs))
			{
				return LogResultLayoutBuilder.getLog(dataAccessResultLogs.get(0));
			}
		}
		else if ("error".equals(tabPath))
		{
			return LogLayoutBuilder.getError(dataAccessLogService.getLog(dlSeq));
		}

		return null;
	}

	@GetMapping("/rfc/execute")
	@ResponseBody
	public Object execute(@RequestParam("dlSeq") String dlSeq)
	{
		return dataAccessLogService.execute(dlSeq);
	}
}