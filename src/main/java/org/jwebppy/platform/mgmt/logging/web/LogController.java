package org.jwebppy.platform.mgmt.logging.web;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import org.jwebppy.platform.mgmt.logging.dto.IfType;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.jwebppy.platform.mgmt.logging.service.DataAccessResultLogService;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileDto;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileListDto;
import org.jwebppy.platform.mgmt.upload.service.UploadFileListService;
import org.jwebppy.platform.mgmt.upload.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/log")
public class LogController extends LoggingGeneralController
{
	private static final String UF_SEQ = "0-684009dc-1cec-4d9b-8a6b-7ab2d9fcf580";

	@Value("${file.upload.rootPath}")
	private String FILE_UPLOAD_ROOT_PATH;


	@Autowired
	private DataAccessLogService dataAccessLogService;

	@Autowired
	private DataAccessResultLogService dataAccessResultLogService;


	@Autowired
	private UploadFileService uploadFileService;

	@Autowired
	private UploadFileListService uploadFileListService;

	@RequestMapping("/list")
	@PreAuthorize("@logAuthorityChecker.hasRead()")
	public String list(Model model, WebRequest webRequest)
	{
		model.addAttribute("fromDate", CmDateFormatUtils.format(CmDateTimeUtils.now().minusDays(1)));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/layout")
	@ResponseBody
	@PreAuthorize("@logAuthorityChecker.hasRead()")
	public Object listLayout(@ModelAttribute DataAccessLogSearchDto dataAccessLogSearch)
	{
		return LogLayoutBuilder.pageableList(new PageableList<>(dataAccessLogService.getPageableLogs(dataAccessLogSearch)));
	}

	@GetMapping("/view")
	@PreAuthorize("@logAuthorityChecker.hasRead()")
	public String view(@ModelAttribute("dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch, Model model, WebRequest webRequest)
	{
		DataAccessLogDto dataAccessLog = dataAccessLogService.getLog(dataAccessLogSearch.getDlSeq());

		if (CollectionUtils.isNotEmpty(dataAccessResultLogService.getSimpleResultLogs(dataAccessLog.getDlSeq())))
		{
			model.addAttribute("fgHasResultLog", PlatformCommonVo.YES);
		}

		model.addAttribute("dataAccessLog", dataAccessLog);

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/view/layout/{tabPath}")
	@ResponseBody
	@PreAuthorize("@logAuthorityChecker.hasRead()")
	public Object viewLayout(@PathVariable("tabPath") String tabPath, @RequestParam("dlSeq") String dlSeq)
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
	@PreAuthorize("@logAuthorityChecker.hasWrite()")
	public Object execute(@RequestParam("dlSeq") String dlSeq)
	{
		return dataAccessLogService.execute(dlSeq);
	}

	@PostMapping("/view/download")
	@ResponseBody
	public Object download(@RequestParam("html") String html, @RequestParam("dlSeq") String dlSeq, @RequestParam("type") String type)
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

		DataAccessLogDto dataAccessLog = dataAccessLogService.getLog(dlSeq);

		String command = (IfType.R.equals(dataAccessLog.getType())) ? dataAccessLog.getCommand() : "sql";

		FileWriter fileWriter = null;

		try
		{
			UploadFileDto uploadFile = uploadFileService.getUploadFile(UF_SEQ);

			String path = FILE_UPLOAD_ROOT_PATH + File.separator + uploadFile.getPath();
			String now = CmDateFormatUtils.defaultZonedFormat(LocalDateTime.now(), PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS);
			String originName = "[" + type.toUpperCase() + "] " + command + "_" + UserAuthenticationUtils.getUsername() + "_" + now + ".html";
			String savedName = System.nanoTime() + ".html";

			FileUtils.forceMkdir(new File(path));

			fileWriter = new FileWriter(path + File.separator + FilenameUtils.getBaseName(savedName));
			fileWriter.write(templete);

			fileWriter.close();
			fileWriter = null;

			UploadFileListDto uploadFileList = uploadFileListService.getUploadFileList(uploadFileListService.save(UF_SEQ, dlSeq, originName, savedName, path));

			return uploadFileList.getDownloadKey();
		}
		catch (IOException e)
		{
			e.printStackTrace();

			if (fileWriter != null)
			{
				try { fileWriter.close(); fileWriter = null; } catch (IOException e1) {};
			}
		}

		return null;
	}

	@GetMapping("/shortcut/list/data")
	@ResponseBody
	@PreAuthorize("@logAuthorityChecker.hasRead()")
	public Object shortcutListData(@ModelAttribute DataAccessLogSearchDto dataAccessLogSearch)
	{
		/*
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setUsername(CmStringUtils.defaultIfEmpty(UserAuthenticationUtils.getUserDetails().getRealUsername(), getUsername()));
		cItemSearch.setType(CItemType.G);
		cItemSearch.setNames(new String[] {"DP_SAP_RFC_LOG_READ", "DP_SAP_RFC_LOG_EXECUTE"});

		boolean hasAuthority = !ListUtils.isEmpty(contentService.getMyItems(cItemSearch));

		if (!hasAuthority)
		{
			throw new AccessDeniedException(i18nMessageSource.getMessage("PLTF_M_NOT_AUTHORIZED"));
		}
		*/

		dataAccessLogSearch.setRegUsername(getUsername());

		return dataAccessLogService.getUsedSapRfcsOnPage(dataAccessLogSearch);
	}
}