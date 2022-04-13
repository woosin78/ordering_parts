package org.jwebppy.portal.iv.survey.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.survey.dto.SurveyDto;
import org.jwebppy.portal.iv.survey.dto.SurveyItemDto;
import org.jwebppy.portal.iv.survey.dto.SurveyQuestionDto;
import org.jwebppy.portal.iv.survey.dto.SurveyTargetDto;
import org.jwebppy.portal.iv.survey.service.SurveyQuestionService;
import org.jwebppy.portal.iv.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/survey/question")
public class SurveyQuestionController extends IvGeneralController
{
	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private SurveyQuestionService surveyQuestionService;
	
	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		int sSeq = Integer.parseInt(webRequest.getParameter("sSeq"));
		model.addAttribute("survey", surveyService.getSurvey(sSeq));
		
		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
	
	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(WebRequest webRequest)
	{
		int sSeq = Integer.parseInt(webRequest.getParameter("sSeq"));
		return ListUtils.emptyIfNull(surveyQuestionService.getSurveyQuestions(sSeq));
	}
	
	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest)
	{
		int sSeq = Integer.parseInt(webRequest.getParameter("sSeq"));
		model.addAttribute("survey", surveyService.getSurvey(sSeq));
		
		if (!CmStringUtils.isEmpty(webRequest.getParameter("sqSeq"))) {
			int sqSeq = Integer.parseInt(webRequest.getParameter("sqSeq"));
			model.addAttribute("surveyQuestion", surveyQuestionService.getSurveyQuestion(sqSeq));
		}
		
		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
	
	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute SurveyQuestionDto surveyQuestion)
	{
		return surveyQuestionService.save(surveyQuestion);
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@ModelAttribute SurveyQuestionDto surveyQuestion)
	{
		
		return surveyQuestionService.delete(surveyQuestion);
	}
}
