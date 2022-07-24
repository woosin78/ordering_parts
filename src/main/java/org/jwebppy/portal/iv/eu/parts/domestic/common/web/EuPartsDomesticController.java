package org.jwebppy.portal.iv.eu.parts.domestic.common.web;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.service.EpBoardContentService;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH)
public class EuPartsDomesticController extends EuPartsDomesticGeneralController
{
	@Autowired
	private EpBoardContentService boardContentService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@RequestMapping("/main")
	public String main(Model model, WebRequest webRequest)
	{
		//공지사항
		String bSeq1 = "0-0f860c26-098b-4a8c-b3d7-374c293bbc91";
		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq(bSeq1);
		boardContentSearch.setRowPerPage(4);
		boardContentSearch.setCorp(getCorp());
		model.addAttribute("bSeq1", bSeq1);
		model.addAttribute("bbs1Name", i18nMessageSource.getMessage(CmStringUtils.upperCase("PLTF_T_" + bSeq1)));
		model.addAttribute("bbs1", ListUtils.emptyIfNull(boardContentService.getPageableBoardContents(boardContentSearch)));

		//Promotion&Policy
		String bSeq2 = "0-00725a1f-18b6-44c4-89a8-5e5531782769";
		boardContentSearch.setBSeq(bSeq2);
		model.addAttribute("bSeq2", bSeq2);
		model.addAttribute("bbs2Name", i18nMessageSource.getMessage(CmStringUtils.upperCase("PLTF_T_PROMOTION")));
		model.addAttribute("bbs2", ListUtils.emptyIfNull(boardContentService.getPageableBoardContents(boardContentSearch)));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}
}