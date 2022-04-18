package org.jwebppy.portal.iv.survey.web;

import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.survey.dto.SurveySearchDto;
import org.jwebppy.portal.iv.survey.service.SurveyService;
import org.jwebppy.portal.iv.survey.service.SurveyVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/survey")
public class SurveyVoteController extends IvGeneralController
{

	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private SurveyVoteService surveyVoteService;
	
	@RequestMapping("/popup/vote")
	public String write(Model model, WebRequest webRequest, SurveySearchDto surveySearchDto)
	{
		model.addAttribute("survey", surveyService.getSurvey(surveySearchDto.getSSeq()));
		
		model.addAttribute("voteItems", surveyVoteService.getSurveyVoteItems(surveySearchDto.getSSeq()));
		
		//UserAuthenticationUtils.getUserDetails().getErpUserContext().getCustCode()
		
		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
}
