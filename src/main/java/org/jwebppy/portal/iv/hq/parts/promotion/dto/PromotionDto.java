package org.jwebppy.portal.iv.hq.parts.promotion.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmMyBatisQueryUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.util.XssHandleUtils;
import org.jwebppy.portal.iv.board.dto.EpBoardContentTargetDto;
import org.jwebppy.portal.iv.hq.parts.common.dto.PartsGeneralDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileListDto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PromotionDto extends PartsGeneralDto
{
	private static final long serialVersionUID = 5443957411620937709L;
	
	private Integer pSeq;
	private String title;
	private String htmlContent;
	private String textContent;
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
	private String fromRegDate;
	private String toRegDate;
	private String writeAuth;
	private String readAuth;
	private String state;
	
	private List<PromotionTargetDto> promotionTargets;
	private List<PromotionItemDto> promotionItems;

	private EpUploadFileDto uploadFile;
	private EpUploadFileDto uploadFile2;
	private List<EpUploadFileListDto> uploadFileLists;
	private List<EpUploadFileListDto> uploadFileLists2;
//	//variables for file upload
	private List<MultipartFile> files;
	private List<MultipartFile> files2;
	private List<String> uflSeqs;
	private List<String> uflSeqs2;

	public String getDisplayFromView()
	{
		return CmDateFormatUtils.format(fromView);
	}

	public String getDisplayToView()
	{
		return CmDateFormatUtils.format(toView);
	}

	public String getFromViewHours()
	{
		return CmDateFormatUtils.defaultZonedFormat(fromView, "HH");
	}

	public String getFromViewMinutes()
	{
		return CmDateFormatUtils.defaultZonedFormat(fromView, "mm");
	}

	public String getToViewHours()
	{
		return CmDateFormatUtils.defaultZonedFormat(toView, "HH");
	}

	public String getToViewMinutes()
	{
		return CmDateFormatUtils.defaultZonedFormat(toView, "mm");
	}

	public String getDisplayTitle()
	{
		return XssHandleUtils.removeTag(title);
	}

	public LocalDateTime getFromView()
	{
		if (CmStringUtils.isNotEmpty(fromViewDate))
		{
			return CmDateTimeUtils.parse(fromViewDate, fromViewHours, fromViewMinutes, "00");
		}

		return null;
	}

	public LocalDateTime getToView()
	{
		if (CmStringUtils.isNotEmpty(toViewDate))
		{
			return CmDateTimeUtils.parse(toViewDate, CmStringUtils.defaultIfEmpty(toViewHours, "23"), CmStringUtils.defaultIfEmpty(toViewMinutes, "59"), "59");
		}

		return null;
	}

	public boolean isMyContent()
	{
		return CmStringUtils.equals(UserAuthenticationUtils.getUsername(), regUsername);
	}
}
