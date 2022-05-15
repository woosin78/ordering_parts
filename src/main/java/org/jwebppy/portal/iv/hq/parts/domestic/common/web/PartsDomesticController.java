package org.jwebppy.portal.iv.hq.parts.domestic.common.web;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.service.EpBoardContentService;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH)
public class PartsDomesticController extends PartsDomesticGeneralController
{
	@Autowired
	private EpBoardContentService boardContentService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@RequestMapping("/main")
	public String main(Model model, WebRequest webRequest)
	{
		//공지사항
		String bSeq1 = "1-07008bda-f80b-4f6c-8397-c382bc344273";
		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq(bSeq1);
		boardContentSearch.setRowPerPage(4);
		boardContentSearch.setCorp(getCorp());
		model.addAttribute("bSeq1", bSeq1);
		model.addAttribute("bbs1Name", i18nMessageSource.getMessage("PLTF_T_" + bSeq1));
		model.addAttribute("bbs1", ListUtils.emptyIfNull(boardContentService.getBoardContents(boardContentSearch)));

		//부품장터
		String bSeq2 = "1-92953403-226b-494e-9c63-55763f8bbb8b";
		boardContentSearch.setBSeq(bSeq2);
		model.addAttribute("bSeq2", bSeq2);
		model.addAttribute("bbs2Name", i18nMessageSource.getMessage("PLTF_T_" + bSeq2));
		model.addAttribute("bbs2", ListUtils.emptyIfNull(boardContentService.getBoardContents(boardContentSearch)));

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