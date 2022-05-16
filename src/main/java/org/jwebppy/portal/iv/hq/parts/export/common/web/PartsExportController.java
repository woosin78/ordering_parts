package org.jwebppy.portal.iv.hq.parts.export.common.web;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.service.EpBoardContentService;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH)
public class PartsExportController extends PartsExportGeneralController
{
	@Autowired
	private EpBoardContentService boardContentService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@RequestMapping("/main")
	public String main(Model model, WebRequest webRequest)
	{
		//공지사항
		String bSeq1 = "0-72bbeac8-8723-4175-ac94-da4e483c7943";
		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq(bSeq1);
		boardContentSearch.setRowPerPage(4);
		boardContentSearch.setCorp(getCorp());
		model.addAttribute("bSeq1", bSeq1);
		model.addAttribute("bbs1Name", i18nMessageSource.getMessage("PLTF_T_" + bSeq1));
		model.addAttribute("bbs1", ListUtils.emptyIfNull(boardContentService.getBoardContents(boardContentSearch)));

		//프로모션&마케팅
		String bSeq2 = "1-fce249eb-f5ac-4818-b89c-2c970a1e5224";
		boardContentSearch.setBSeq(bSeq2);
		model.addAttribute("bSeq2", bSeq2);
		model.addAttribute("bbs2Name", i18nMessageSource.getMessage("PLTF_T_" + bSeq2));
		model.addAttribute("bbs2", ListUtils.emptyIfNull(boardContentService.getBoardContents(boardContentSearch)));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}
}
