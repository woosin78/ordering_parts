package org.jwebppy.portal.iv.survey.dto;

import java.util.List;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileListDto;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;
	
	private int sSeq;
	private String title;
	private String description;
	private String validFrom;
	private String validFromYmd;
	private String validTo;
	private String validToYmd;
	private String fgOpen;
	private String fgOpenResult;
	private String remark;
	
	private String fgStatus;
	private String applier;

	private List<SurveyTargetDto> surveyTargets;
	
	//variables for file upload
	private EpUploadFileDto uploadFile;
	private List<EpUploadFileListDto> uploadFileLists;
	private List<MultipartFile> files;
	private List<String> uflSeqs;

}
