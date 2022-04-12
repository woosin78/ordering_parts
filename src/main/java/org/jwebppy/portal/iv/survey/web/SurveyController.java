package org.jwebppy.portal.iv.survey.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.survey.dto.SurveyDto;
import org.jwebppy.portal.iv.survey.dto.SurveySearchDto;
import org.jwebppy.portal.iv.survey.dto.SurveyTargetDto;
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
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/survey")
public class SurveyController extends IvGeneralController
{

	@Autowired
	private SurveyService surveyService;
	
	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
	
	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute SurveySearchDto surveySearchDto)
	{
		return ListUtils.emptyIfNull(surveyService.getSurveys(surveySearchDto));
	}
	
	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest, SurveySearchDto surveySearchDto)
	{
		int sSeq = surveySearchDto.getSSeq();
		if (sSeq > 0) {
			model.addAttribute("survey", surveyService.getSurvey(surveySearchDto.getSSeq()));
		}

		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
	
	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest, SurveySearchDto surveySearchDto)
	{
		int sSeq = surveySearchDto.getSSeq();
		if (sSeq > 0) {
			model.addAttribute("survey", surveyService.getSurvey(surveySearchDto.getSSeq()));
		}

		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
	
	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute SurveyDto survey, @RequestParam(name = "targetCode", required = false) String[] targetCodes, @RequestParam(name = "targetDescription", required = false) String[] targetDescriptions)
	{
		if (ArrayUtils.isNotEmpty(targetCodes))
		{
			List<SurveyTargetDto> surveyTargets = new ArrayList<>();
			int index = 0;

			for (String targetCode: targetCodes)
			{
				SurveyTargetDto surveyTarget = new SurveyTargetDto();
				//surveyTarget.setSSeq(survey.getSSeq());
				surveyTarget.setTarget(targetCode);
				surveyTarget.setTargetName(targetDescriptions[index]);
				surveyTarget.setType("D");//D:DealerepBoardContentTarget.setType("D");//D:Dealer
				
				surveyTargets.add(surveyTarget);

				index++;
			}
			
			survey.setSurveyTargets(surveyTargets);
		}
		
		try {
			
			return surveyService.save(survey);
		} 
		catch (IOException e) 
		{
			System.err.println(e);
		}
		
		return null;
	}
	
	@RequestMapping("/popup/view")
	public String preView(Model model, WebRequest webRequest, SurveySearchDto surveySearchDto)
	{
		addAllAttributeFromRequest(model, webRequest);
		
		return DEFAULT_VIEW_URL;
	}
}
