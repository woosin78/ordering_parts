package org.jwebppy.portal.iv.survey.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.survey.dto.SurveyDto;
import org.jwebppy.portal.iv.survey.dto.SurveyItemDto;
import org.jwebppy.portal.iv.survey.dto.SurveyQuestionDto;
import org.jwebppy.portal.iv.survey.dto.SurveySearchDto;
import org.jwebppy.portal.iv.survey.dto.SurveyTargetDto;
import org.jwebppy.portal.iv.survey.entity.SurveyEntity;
import org.jwebppy.portal.iv.survey.mapper.SurveyMapper;
import org.jwebppy.portal.iv.survey.mapper.SurveyObjectMapper;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileListDto;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.jwebppy.portal.iv.upload.service.EpUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyService extends PlatformGeneralService
{
	@Autowired
	private SurveyMapper surveyMapper;
	
	@Autowired
	private SurveyTargetService surveyTargetService;
	
	@Autowired
	private SurveyItemService surveyItemService;

	@Autowired
	private SurveyQuestionService surveyQuestionService;
	
	@Autowired
	private EpUploadFileListService uploadFileListService;
	
	@Autowired
	private EpUploadFileService uploadFileService;
	
	public final String UPLOAD_NAME = "SURVEY";
	
	public SurveyDto getSurvey(int sSeq) 
	{
		SurveyDto survey = new SurveyDto();
		survey.setSSeq(sSeq);
		
		return getSurvey(survey);
	}
	
	public SurveyDto getSurvey(SurveyDto survey) 
	{
		return CmModelMapperUtils.mapToDto(SurveyObjectMapper.INSTANCE, surveyMapper.findSurvey(survey));
	}

	public List<SurveyDto> getSurveys(SurveySearchDto surveySearchDto) 
	{
		surveySearchDto.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		return CmModelMapperUtils.mapToDto(SurveyObjectMapper.INSTANCE, surveyMapper.findSurveys(surveySearchDto));
	}
	
	public int save(SurveyDto survey) throws IOException
	{
		if (survey.getSSeq() == 0) {
			return create(survey);
		} 
		else 
		{
			return modify(survey);
		}
	}
	
	public int create(SurveyDto survey) throws IOException
	{
		SurveyEntity surveyEntity = CmModelMapperUtils.mapToEntity(SurveyObjectMapper.INSTANCE, survey);
		surveyMapper.insert(surveyEntity);
		int sSeq = surveyEntity.getSSeq();
		
		EpUploadFileDto uploadFile = uploadFileService.getUploadFileByName(UPLOAD_NAME);
		if (ObjectUtils.isNotEmpty(uploadFile)) {
			if (uploadFileListService.save(uploadFile, Integer.toString(sSeq), survey.getFiles()) > 0) {
				surveyEntity.setUfSeq(uploadFile.getUfSeq());
				surveyMapper.updateUfSeq(surveyEntity);
			}
		}
		
		if (ObjectUtils.isNotEmpty(survey.getSurveyTargets())) {
			for (SurveyTargetDto surveyTarget: survey.getSurveyTargets()) {
				surveyTarget.setSSeq(sSeq);
			}
			surveyTargetService.save(survey.getSurveyTargets());
		}
		
		return sSeq;
	}
	
	public int modify(SurveyDto survey) throws IOException
	{
		SurveyEntity surveyEntity = CmModelMapperUtils.mapToEntity(SurveyObjectMapper.INSTANCE, survey);
		surveyMapper.update(surveyEntity);
		int sSeq = surveyEntity.getSSeq();
		
		EpUploadFileDto uploadFile = uploadFileService.getUploadFileByName(UPLOAD_NAME);
		if (ObjectUtils.isNotEmpty(uploadFile)) {
			if (uploadFileListService.save(uploadFile, Integer.toString(sSeq), survey.getFiles()) > 0) {
				surveyEntity.setUfSeq(uploadFile.getUfSeq());
				surveyMapper.updateUfSeq(surveyEntity);
			}
		}
		uploadFileListService.delete(survey.getUflSeqs());
		
		surveyTargetService.save(survey);
		
		return sSeq;
	}
	
	public int delete(SurveyDto survey) throws IOException
	{
		// to-do 삭제불가로직
		
		int sSeq = survey.getSSeq();
		
		// item
		SurveyItemDto surveyItem = new SurveyItemDto();
		surveyItem.setSSeq(sSeq);
		surveyItemService.deleteBySseq(surveyItem);
		
		// question
		SurveyQuestionDto surveyQuestion = new SurveyQuestionDto();
		surveyQuestion.setSSeq(sSeq);
		surveyQuestionService.deleteBySseq(surveyQuestion);
		
		// taraget
		SurveyTargetDto surveyTarget = new SurveyTargetDto();
		surveyTarget.setSSeq(sSeq);
		surveyTargetService.delete(surveyTarget);
		
		// files
		List<EpUploadFileListDto> fileList = uploadFileListService.getUploadFileLists(surveyMapper.findUfSeq(sSeq), Integer.toString(sSeq));
		if (ObjectUtils.isNotEmpty(fileList)) {
			for (EpUploadFileListDto file : fileList) {
				uploadFileListService.delete(file.getUflSeq()); 
			}
		}
		
		// survey
		SurveyEntity surveyEntity = CmModelMapperUtils.mapToEntity(SurveyObjectMapper.INSTANCE, survey);
		surveyMapper.delete(surveyEntity);
		
		return 1;
	}
}
