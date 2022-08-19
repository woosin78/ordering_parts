package org.jwebppy.portal.iv.hq.parts.promotion.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.portal.iv.hq.parts.common.entity.PartsGeneralEntity;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileEntity;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileListEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PromotionEntity extends PartsGeneralEntity
{
	private static final long serialVersionUID = 5555926427143731047L;
	
	private Integer pSeq;
	private String title;
	private String textContent;
	private String htmlContent;
	private LocalDateTime fromView;
	private LocalDateTime toView;
	private String fromViewDate;
	private String fromViewHours;
	private String fromViewMinutes;
	private String toViewDate;
	private String toViewHours;
	private String toViewMinutes;
	private String target;
	private String ufSeq;
	private String writer;
	private String width;
	private String height;
	private String fgDelete;
	private String fromRegDate;
	private String toRegDate;
	private String writeAuth;
	private String readAuth;
	private String state;

	private List<PromotionTargetEntity> promotionTargets;
	private List<PromotionItemEntity> promotionItems;

	private EpUploadFileEntity uploadFile;
	private EpUploadFileEntity uploadFile2;
	private List<EpUploadFileListEntity> uploadFileLists;
	private List<EpUploadFileListEntity> uploadFileLists2;
//	//variables for file upload
	private List<MultipartFile> files;
	private List<MultipartFile> files2;
	private List<String> uflSeqs;
	private List<String> uflSeqs2;
	
}
