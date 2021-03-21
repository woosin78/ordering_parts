package org.jwebppy.platform.mgmt.logging.web;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.logging.LoggingGeneralController;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessResultLogDto;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;
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

	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	@RequestMapping("/list")
	public String list(@ModelAttribute(value = "dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch)
	{
		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute DataAccessLogSearchDto dataAccessLogSearch)
	{
		return LogLayoutBuilder.getList(new PageableList<>(dataAccessLogService.getPageableLogs(dataAccessLogSearch)));
	}

	@GetMapping("/detail_popup")
	public String detailPopup(@ModelAttribute("dataAccessLogSearch") DataAccessLogSearchDto dataAccessLogSearch, Model model)
	{
		model.addAttribute("dataAccessLog", dataAccessLogService.getLog(dataAccessLogSearch.getDlSeq()));

		if (CollectionUtils.isNotEmpty(dataAccessResultLogService.getResultLogs(dataAccessLogSearch.getDlSeq())))
		{
			dataAccessLogSearch.setFgHasResultLog(PlatformCommonVo.YES);
		}

		return DEFAULT_VIEW_URL;
	}

	@GetMapping("/detail_popup/{tabPath}")
	@ResponseBody
	public Object log(@PathVariable("tabPath") String tabPath, @RequestParam("dlSeq") String dlSeq)
	{
		if ("parameter".equals(tabPath))
		{
			return LogLayoutBuilder.getLog(dataAccessLogService.getLog(dlSeq));
		}
		else
		{
			List<DataAccessResultLogDto> dataAccessResultLogs = dataAccessResultLogService.getResultLogs(dlSeq);

			if (CollectionUtils.isNotEmpty(dataAccessResultLogs))
			{
				return LogResultLayoutBuilder.getLog(dataAccessResultLogs.get(0));
			}
		}

		return null;
	}

	@GetMapping("/rfc/execute")
	@ResponseBody
	public Object execute(@RequestParam("dlSeq") String dlSeq)
	{
		DataAccessLogDto dataAccessLog = dataAccessLogService.getLog(dlSeq);

		RfcRequest rfcRequest = new RfcRequest(dataAccessLog.getCommand());

		List<DataAccessLogParameterDto> dataAccessLogParameters = dataAccessLog.getDataAccessLogParameters();

		if (CollectionUtils.isNotEmpty(dataAccessLogParameters))
		{
			for (DataAccessLogParameterDto dataAccessLogParameter: dataAccessLogParameters)
			{
				ParameterType type = dataAccessLogParameter.getType();
				String name = dataAccessLogParameter.getName();
				List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails = dataAccessLogParameter.getDataAccessLogParameterDetails();

				if (CollectionUtils.isNotEmpty(dataAccessLogParameterDetails))
				{
					if (ParameterType.T.equals(type))
					{
						rfcRequest.addTable(name, dataAccessLogParameterDetails);
					}
					else
					{
						for (DataAccessLogParameterDetailDto dataAccessLogParameterDetail: dataAccessLogParameterDetails)
						{
							if (ParameterType.F.equals(type))
							{
								rfcRequest.addField(name, dataAccessLogParameterDetail.getValue());
							}
							else if (ParameterType.S.equals(type))
							{
								rfcRequest.addStructure(name, dataAccessLogParameterDetail.getName(), dataAccessLogParameterDetail.getValue());
							}
						}
					}
				}
			}
		}

		return simpleRfcTemplate.response(rfcRequest);
	}
}