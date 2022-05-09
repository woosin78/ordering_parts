package org.jwebppy.portal.iv.survey.web;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.survey.dto.SurveyItemDto;
import org.jwebppy.portal.iv.survey.service.SurveyItemService;
import org.jwebppy.portal.iv.survey.service.SurveyQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/survey/item")
@PreAuthorize("hasAnyRole('ROLE_DP_IVDO_PARTS_MANAGER', 'ROLE_DP_IVEX_PARTS_MANAGER')")
public class SurveyItemController extends IvGeneralController
{
	@Autowired
	private SurveyItemService surveyItemService;

	@Autowired
	private SurveyQuestionService surveyQuestionService;

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest)
	{
		int sqSeq = Integer.parseInt(webRequest.getParameter("sqSeq"));
		model.addAttribute("surveyQuestion", surveyQuestionService.getSurveyQuestion(sqSeq));

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(WebRequest webRequest)
	{
		int sqSeq = Integer.parseInt(webRequest.getParameter("sqSeq"));
		return ListUtils.emptyIfNull(surveyItemService.getSurveyItems(sqSeq));
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute SurveyItemDto surveyItem)
	{

		return surveyItemService.save(surveyItem);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@ModelAttribute SurveyItemDto surveyItem)
	{

		return surveyItemService.delete(surveyItem);
	}

}
