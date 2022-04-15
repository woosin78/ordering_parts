package org.jwebppy.portal.iv.survey.entity;

import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileEntity;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileListEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyEntity extends GeneralEntity
{
	private static final long serialVersionUID = -5090253475844881449L;
	
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
	
	private List<SurveyTargetEntity> surveyTargets;
	
	private String ufSeq;
	private EpUploadFileEntity uploadFile;
	private List<EpUploadFileListEntity> uploadFileLists;
	
}
