package org.jwebppy.portal.iv.hq.parts.promotion.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmArrayUtils;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.Formatter;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.iv.hq.parts.common.PartsCommonVo;
import org.jwebppy.portal.iv.hq.parts.common.web.PartsGeneralController;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionDto;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionItemDto;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionSearchDto;
import org.jwebppy.portal.iv.hq.parts.promotion.dto.PromotionTargetDto;
import org.jwebppy.portal.iv.hq.parts.promotion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Controller
@RequestMapping(PartsCommonVo.REQUEST_PATH + "/promotion")
public class PromotionController extends PartsGeneralController
{
	@Autowired
	private PromotionService promotionService;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest) // , PromotionSearchDto promotionSearch
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute PromotionSearchDto promotionSearch, WebRequest webRequest)
	{
		promotionSearch.setCorp(getCorp());
		promotionSearch.setRowPerPage(999999);

		return ListUtils.emptyIfNull(promotionService.getPageablePromotions(promotionSearch));
	}

	@RequestMapping("/list/banner")
	@ResponseBody
	public Object listBanner(@ModelAttribute PromotionSearchDto promotionSearch, WebRequest webRequest)
	{
		// 프로모션 대상 조건
		promotionSearch.setCustCode(getErpUserContext().getCustCode());

		return ListUtils.emptyIfNull(promotionService.getBannerPromotions(promotionSearch));
	}

	@RequestMapping("/popup/view")
	public String popupView(Model model, WebRequest webRequest, PromotionSearchDto promotionSearch)
	{
		if (ObjectUtils.isNotEmpty(promotionSearch.getPSeq()))
		{
			model.addAttribute("promotion", promotionService.getPromotion(promotionSearch.getPSeq(), getErpUserInfo()));
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest, PromotionSearchDto promotionSearch)
	{
		if (ObjectUtils.isNotEmpty(promotionSearch.getPSeq()))
		{
			model.addAttribute("promotion", promotionService.getPromotion(promotionSearch.getPSeq(), getErpUserInfo()));
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute PromotionDto promotion,
			@RequestParam(name = "targetCode", required = false) String[] targetCodes,
			@RequestParam(name = "targetDescription", required = false) String[] targetDescriptions,
			@RequestParam(name = "materialNo", required = false) String[] materialNos
			)
	{

		promotion.setTarget(getErpUserContext().getSalesOrg() + getErpUserContext().getDistChl() );

		if (CmArrayUtils.isNotEmpty(targetCodes))
		{
			List<PromotionTargetDto> promotionTargets = new ArrayList<>();
			int index = 0;

			for (String targetCode: targetCodes)
			{
				PromotionTargetDto promotionTarget = new PromotionTargetDto();
				promotionTarget.setPSeq(promotion.getPSeq());
				promotionTarget.setCode(targetCode);
				promotionTarget.setDescription(targetDescriptions[index]);
				promotionTarget.setType("D");//D:Dealer

				promotionTargets.add(promotionTarget);

				index++;
			}
			promotion.setPromotionTargets(promotionTargets);
		}

		if (CmArrayUtils.isNotEmpty(materialNos))
		{
			List<PromotionItemDto> promotionItems = new ArrayList<>();
			for (String materialNo: materialNos)
			{
				PromotionItemDto promotionItem = new PromotionItemDto();
				promotionItem.setPSeq(promotion.getPSeq());
				promotionItem.setMaterialNo(materialNo);

				promotionItems.add(promotionItem);
			}
			promotion.setPromotionItems(promotionItems);
		}

		try
		{
			return promotionService.save(promotion);
		}
		catch (MaxUploadSizeExceededException e)
		{
			PromotionDto p = promotionService.getPromotion(promotion.getPSeq(), getErpUserInfo());

			if (ObjectUtils.isNotEmpty(p.getUploadFile()))
			{
				return i18nMessageSource.getMessage("HQP_M_EXCEED_MAXIMUM_UPLOAD_SIZE", new String[] { Formatter.getDisplayFileSize(p.getUploadFile().getMaxFileSize()) } );
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest, PromotionSearchDto promotionSearch)
	{
		if (ObjectUtils.isNotEmpty(promotionSearch.getPSeq()))
		{
			model.addAttribute("promotion", promotionService.getPromotion(promotionSearch.getPSeq(), getErpUserInfo()));
		}

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(PromotionDto promotion)
	{
		return promotionService.delete(promotion);
	}

	protected void setDefaultAttribute(Model model, WebRequest webRequest)
	{
		PromotionSearchDto promotionSearch = new PromotionSearchDto();
		promotionSearch.setPSeq(Integer.getInteger(webRequest.getParameter("pSeq")));
		promotionSearch.setTitle(webRequest.getParameter("title"));
		promotionSearch.setFromRegDate(webRequest.getParameter("fromRegDate"));
		promotionSearch.setToRegDate(webRequest.getParameter("toRegDate"));
		promotionSearch.setState(webRequest.getParameter("state"));
		promotionSearch.setPageNumber(CmNumberUtils.toInt(webRequest.getParameter("pageNumber"), 1));
		promotionSearch.setRowPerPage(CmNumberUtils.toInt(webRequest.getParameter("rowPerPage"), PlatformCommonVo.DEFAULT_ROW_PER_PAGE));

		model.addAttribute("promotionSearch", promotionSearch);

		addAllAttributeFromRequest(model, webRequest);
	}
}



