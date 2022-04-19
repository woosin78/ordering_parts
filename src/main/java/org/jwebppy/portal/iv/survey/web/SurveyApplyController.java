package org.jwebppy.portal.iv.survey.web;

import java.util.List;

import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.survey.dto.SurveySearchDto;
import org.jwebppy.portal.iv.survey.dto.SurveyApplyDto;
import org.jwebppy.portal.iv.survey.service.SurveyService;
import org.jwebppy.portal.iv.survey.service.SurveyApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/survey")
public class SurveyApplyController extends IvGeneralController
{

	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private SurveyApplyService surveyApplyService;
	

	@RequestMapping("/popup/view")
	public String preView(Model model, WebRequest webRequest, SurveySearchDto surveySearchDto)
	{
		model.addAttribute("survey", surveyService.getSurvey(surveySearchDto.getSSeq()));
		
		model.addAttribute("voteItems", surveyApplyService.getSurveyVoteItems(surveySearchDto.getSSeq()));
		
		//UserAuthenticationUtils.getUserDetails().getErpUserContext().getCustCode()
		
		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
	
	@RequestMapping("/popup/vote")
	public String write(Model model, WebRequest webRequest, SurveySearchDto surveySearchDto)
	{
		model.addAttribute("survey", surveyService.getSurvey(surveySearchDto.getSSeq()));
		
		model.addAttribute("voteItems", surveyApplyService.getSurveyVoteItems(surveySearchDto.getSSeq()));
		
		//UserAuthenticationUtils.getUserDetails().getErpUserContext().getCustCode()
		
		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/result/view")
	public String result(Model model, WebRequest webRequest, SurveySearchDto surveySearchDto)
	{
		model.addAttribute("survey", surveyService.getSurvey(surveySearchDto.getSSeq()));
		
		//model.addAttribute("voteItems", surveyApplyService.getSurveyResult(surveySearchDto.getSSeq()));
		
		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
	
	@PostMapping("/vote/save")
	@ResponseBody
	public Object save(@ModelAttribute SurveyApplyDto surveyApplys)
	{
		return surveyApplyService.save(surveyApplys);
	}
	
}
