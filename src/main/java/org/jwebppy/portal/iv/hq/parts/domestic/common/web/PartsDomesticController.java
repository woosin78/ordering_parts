package org.jwebppy.portal.iv.hq.parts.domestic.common.web;

import org.apache.commons.collections4.ListUtils;
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

	@RequestMapping("/main")
	public String main(Model model, WebRequest webRequest)
	{
		//공지사항
		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq("1-07008bda-f80b-4f6c-8397-c382bc344273");
		boardContentSearch.setRowPerPage(4);
		boardContentSearch.setCorp(getCorp());
		model.addAttribute("notice", ListUtils.emptyIfNull(boardContentService.getBoardContents(boardContentSearch)));

		//부품장터
		boardContentSearch.setBSeq("1-92953403-226b-494e-9c63-55763f8bbb8b");
		model.addAttribute("market", ListUtils.emptyIfNull(boardContentService.getBoardContents(boardContentSearch)));

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