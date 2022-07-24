package org.jwebppy.portal.iv.uk.parts.domestic.common.web;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.service.EpBoardContentService;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(UkPartsDomesticCommonVo.REQUEST_PATH)
public class UkPartsDomesticController extends UkPartsDomesticGeneralController
{
	@Autowired
	private EpBoardContentService boardContentService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@RequestMapping("/main")
	public String main(Model model, WebRequest webRequest)
	{
		//공지사항
		String bSeq1 = "0-889a2868-ef2e-481d-a4ff-8ac269ebb800";
		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq(bSeq1);
		boardContentSearch.setRowPerPage(4);
		boardContentSearch.setCorp(getCorp());
		model.addAttribute("bSeq1", bSeq1);
		model.addAttribute("bbs1Name", i18nMessageSource.getMessage(CmStringUtils.upperCase("PLTF_T_" + bSeq1)));
		model.addAttribute("bbs1", ListUtils.emptyIfNull(boardContentService.getPageableBoardContents(boardContentSearch)));

		//Promotion&Policy
		String bSeq2 = "1-78d985aa-3a6c-424d-a38c-8f5ff3ee133e";
		boardContentSearch.setBSeq(bSeq2);
		model.addAttribute("bSeq2", bSeq2);
		model.addAttribute("bbs2Name", i18nMessageSource.getMessage(CmStringUtils.upperCase("PLTF_T_PROMOTION")));
		model.addAttribute("bbs2", ListUtils.emptyIfNull(boardContentService.getPageableBoardContents(boardContentSearch)));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}
}