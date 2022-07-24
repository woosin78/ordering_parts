package org.jwebppy.portal.iv.board.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmArrayUtils;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.Formatter;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.common.PortalCommonVo;
import org.jwebppy.portal.iv.board.dto.EpBoardContentDto;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.dto.EpBoardContentTargetDto;
import org.jwebppy.portal.iv.board.dto.EpBoardDto;
import org.jwebppy.portal.iv.board.service.EpBoardContentService;
import org.jwebppy.portal.iv.board.service.EpBoardService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.google.common.collect.ImmutableMap;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/board")
public class EpBoardContentController extends IvGeneralController
{
	@Autowired
	private EpBoardService boardService;

	@Autowired
	private EpBoardContentService boardContentService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@Autowired
	private EpUploadFileListService uploadFileListService;

	public static void main(String[] args)
	{
		System.err.println(UidGenerateUtils.generate());
		System.err.print(UidGenerateUtils.generate());
	}

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest, EpBoardContentSearchDto boardContentSearch)
	{
		//Doobiz 에 있는 과거 데이터를 보여주기 위한 용도
		addOldMenuLink(model, boardContentSearch.getBSeq());

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute EpBoardContentSearchDto boardContentSearch, WebRequest webRequest)
	{
		boardContentSearch.setCorp(getCorp());
		boardContentSearch.setRowPerPage(999999);

		return ListUtils.emptyIfNull(boardContentService.getPageableBoardContents(boardContentSearch));
	}

	@RequestMapping("/popup/list/data")
	@ResponseBody
	public Object popupListData(@ModelAttribute EpBoardContentSearchDto boardContentSearch, WebRequest webRequest)
	{
		boardContentSearch.setCorp(getCorp());
		boardContentSearch.setBSeq("0-e9267a6b-69ce-445d-a683-42f6cf916787");
		boardContentSearch.setCustCode(getErpUserContext().getCustCode());
		boardContentSearch.setFgPopup(PortalCommonVo.YES);

		return ListUtils.emptyIfNull(boardContentService.getPageableBoardContents(boardContentSearch));
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		viewProc(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/popup/view")
	public Object viewPopup(Model model, WebRequest webRequest)
	{
		viewProc(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/popup/view2")
	public Object viewPopup2(Model model, WebRequest webRequest)
	{
		viewProc(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	protected void viewProc(Model model, WebRequest webRequest)
	{
		String bcSeq = webRequest.getParameter("bcSeq");

		boardContentService.plusViews(bcSeq);

		EpBoardContentDto boardContent = boardContentService.getBoardContent(bcSeq);
		EpBoardDto board = boardContent.getBoard();
		EpUploadFileDto uploadFile = board.getUploadFile();

		if (uploadFile != null)
		{
			boardContent.setUploadFileLists(uploadFileListService.getUploadFileLists(uploadFile.getUfSeq(), boardContent.getBcSeq()));
		}

		model.addAttribute("boardContent", boardContent);

		setDefaultAttribute(model, webRequest);
	}

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest, EpBoardContentSearchDto boardContentSearch)
	{
		model.addAttribute("boardContent", boardContentService.getBoardContent(boardContentSearch.getBcSeq()));

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute EpBoardContentDto boardContent, @RequestParam(name = "targetCode", required = false) String[] targetCodes, @RequestParam(name = "targetDescription", required = false) String[] targetDescriptions)
	{
		if (CmArrayUtils.isNotEmpty(targetCodes))
		{
			List<EpBoardContentTargetDto> boardContentTargets = new ArrayList<>();
			int index = 0;

			for (String targetCode: targetCodes)
			{
				EpBoardContentTargetDto epBoardContentTarget = new EpBoardContentTargetDto();
				epBoardContentTarget.setBcSeq(boardContent.getBcSeq());
				epBoardContentTarget.setCode(targetCode);
				epBoardContentTarget.setDescription(targetDescriptions[index]);
				epBoardContentTarget.setType("D");//D:Dealer

				boardContentTargets.add(epBoardContentTarget);

				index++;
			}

			boardContent.setBoardContentTargets(boardContentTargets);
		}

		try
		{
			return boardContentService.save(boardContent);
		}
		catch (MaxUploadSizeExceededException e)
		{
			EpBoardDto board = boardService.getBoard(boardContent.getBcSeq());

			if (board.getUploadFile() != null)
			{
				return i18nMessageSource.getMessage("HQP_M_EXCEED_MAXIMUM_UPLOAD_SIZE", new String[] { Formatter.getDisplayFileSize(board.getUploadFile().getMaxFileSize()) } );
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("bcSeq") List<String> bcSeqs)
	{
		return boardContentService.delete(bcSeqs);
	}

	protected void setDefaultAttribute(Model model, WebRequest webRequest)
	{
		String bSeq = webRequest.getParameter("bSeq");

		EpBoardDto board = boardService.getBoard(webRequest.getParameter("bSeq"));

		if (!UserAuthenticationUtils.hasRole(CmStringUtils.split(board.getReadAuth(), IvCommonVo.DELIMITER)))
		{
			throw new AccessDeniedException(i18nMessageSource.getMessage("PLTF_M_NOT_AUTHORIZED"));
		}

		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq(bSeq);
		boardContentSearch.setBcSeq(webRequest.getParameter("bcSeq"));
		boardContentSearch.setTitle(webRequest.getParameter("title"));
		boardContentSearch.setWriter(webRequest.getParameter("writer"));
		boardContentSearch.setFromRegDate(webRequest.getParameter("fromRegDate"));
		boardContentSearch.setToRegDate(webRequest.getParameter("toRegDate"));
		boardContentSearch.setFromView(webRequest.getParameter("fromView"));
		boardContentSearch.setToView(webRequest.getParameter("toView"));
		boardContentSearch.setPageNumber(CmNumberUtils.toInt(webRequest.getParameter("pageNumber"), 1));
		boardContentSearch.setRowPerPage(CmNumberUtils.toInt(webRequest.getParameter("rowPerPage"), PlatformCommonVo.DEFAULT_ROW_PER_PAGE));

		model.addAttribute("boardContentSearch", boardContentSearch);
		model.addAttribute("board", board);
		model.addAttribute("boardName", i18nMessageSource.getMessage(CmStringUtils.upperCase("PLTF_T_" + bSeq)));
		model.addAttribute("isManager", isManager());
		model.addAttribute("fgFrom", webRequest.getParameter("fgFrom"));//M: come from main page
		model.addAttribute("hasWriteAuth", UserAuthenticationUtils.hasRole(CmStringUtils.split(board.getWriteAuth(), IvCommonVo.DELIMITER)));

		addAllAttributeFromRequest(model, webRequest);
	}

	public void addOldMenuLink(Model model, String bSeq)
	{
		/*
		 * DIVK 내수의 경우 공지사항, 부품장버는 과거 데이터(Doobiz)는 보여줌
		 * 공지사항: 1-07008bda-f80b-4f6c-8397-c382bc344273, /irj/servlet/prt/portal/prtroot/pcd!3aportal_content!2fdoosan_infracore_2nd!2fcommon!2froles!2fparts!2fcom.doosaninfracore.r.parts_dido_iv!2fcom.doosaninfracore.w.home_dido!2fcom.doosaninfracore.i.20090917_notice_dido?InitialNodeFirstLevel=true&windowId=WID1655167963312
		 * 부품장터: 1-92953403-226b-494e-9c63-55763f8bbb8b, /irj/servlet/prt/portal/prtroot/pcd!3aportal_content!2fdoosan_infracore_2nd!2fcommon!2froles!2fparts!2fcom.doosaninfracore.r.parts_dido_iv!2fcom.doosaninfracore.w.home_dido!2fcom.doosaninfracore.i.20090917_market_dido?InitialNodeFirstLevel=true&windowId=WID1655167963312
		*/
		ImmutableMap<String, String> oldSystemUrlMap = new ImmutableMap.Builder<String, String>()
				.put("1-07008bda-f80b-4f6c-8397-c382bc344273", "/irj/servlet/prt/portal/prtroot/pcd!3aportal_content!2fdoosan_infracore_2nd!2fcommon!2froles!2fparts!2fcom.doosaninfracore.r.parts_dido_iv!2fcom.doosaninfracore.w.home_dido!2fcom.doosaninfracore.i.20090917_notice_dido?InitialNodeFirstLevel=true&windowId=WID1655167963312")
				.put("1-92953403-226b-494e-9c63-55763f8bbb8b", "/irj/servlet/prt/portal/prtroot/pcd!3aportal_content!2fdoosan_infracore_2nd!2fcommon!2froles!2fparts!2fcom.doosaninfracore.r.parts_dido_iv!2fcom.doosaninfracore.w.home_dido!2fcom.doosaninfracore.i.20090917_market_dido?InitialNodeFirstLevel=true&windowId=WID1655167963312")
				.build();

		String oldSystemUrl = oldSystemUrlMap.get(bSeq);

		if (CmStringUtils.isNotEmpty(oldSystemUrl))
		{
			oldSystemUrl = getDoobizDomain() + oldSystemUrl;
		}

		model.addAttribute("oldSystemUrl", oldSystemUrl);
	}
}
