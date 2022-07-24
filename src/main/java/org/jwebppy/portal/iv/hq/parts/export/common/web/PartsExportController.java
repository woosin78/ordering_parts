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
		String bSeq1 = "0-2b5ef5a9-fd53-43b7-9190-0c27dfb26040";
		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq(bSeq1);
		boardContentSearch.setRowPerPage(4);
		boardContentSearch.setCorp(getCorp());
		model.addAttribute("bSeq1", bSeq1);
		model.addAttribute("bbs1Name", i18nMessageSource.getMessage("PLTF_T_" + bSeq1));
		model.addAttribute("bbs1", ListUtils.emptyIfNull(boardContentService.getPageableBoardContents(boardContentSearch)));

		//프로모션&마케팅
		String bSeq2 = "1-12886034-ac64-4d5d-a874-3245a127d73c";
		boardContentSearch.setBSeq(bSeq2);
		model.addAttribute("bSeq2", bSeq2);
		model.addAttribute("bbs2Name", i18nMessageSource.getMessage("PLTF_T_PROMOTION"));
		model.addAttribute("bbs2", ListUtils.emptyIfNull(boardContentService.getPageableBoardContents(boardContentSearch)));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/preload")
	public String preload(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}
}
