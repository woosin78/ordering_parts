package org.jwebppy.platform.mgmt.logging.web;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.logging.LoggingGeneralController;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessResultLogDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.jwebppy.platform.mgmt.logging.service.DataAccessResultLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/log")
public class LogController extends LoggingGeneralController
{
	@Autowired
	private DataAccessLogService dataAccessLogService;

	@Autowired
	private DataAccessResultLogService dataAccessResultLogService;

	@Value("${file.upload.rootPath}")
	private String path;

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

	@PostMapping("/view/download")
	@ResponseBody
	public Object download(@RequestParam("html") String html, @RequestParam("functionName") String functionName)
	{
		String templete = "<!DOCTYPE html>";
		templete += "<html>";
		templete += "<head>";
		templete += "<title></title>";
		templete += "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />";
		templete += "<meta http-equiv='Pragma' content='no-cache' />";
		templete += "<meta http-equiv='Expires' content='-1' />";
		templete += "<meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no' />";
		templete += "<link rel='stylesheet' type='text/css' class='ui' href='https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.4.1/semantic.min.css' />";
		templete += "</head>";
		templete += "<body>";
		templete += "<div class='main container'>";
		templete += "<div class='ui segment teal'>";
		templete += html;
		templete += "</div>";
		templete += "</div>";
		templete += "</body>";
		templete += "</html>";

		String savedFileName = path + File.separator + functionName + "-" + UserAuthenticationUtils.getUsername() + "-" + CmDateFormatUtils.format(CmDateTimeUtils.now(), PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS) + ".html";

		FileWriter fileWriter = null;

		try
		{
			fileWriter = new FileWriter(savedFileName);
			fileWriter.write(templete);

			fileWriter.close();
			fileWriter = null;
		}
		catch (IOException e)
		{
			e.printStackTrace();

			if (fileWriter != null)
			{
				try { fileWriter.close(); fileWriter = null; } catch (IOException e1) {};
			}
		}

		return savedFileName;
	}
}