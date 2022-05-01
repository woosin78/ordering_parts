package org.jwebppy.portal.iv.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.XssHandleUtils;
import org.jwebppy.portal.iv.common.dto.IvGeneralDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileListDto;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpBoardContentDto extends IvGeneralDto
{
	private static final long serialVersionUID = -1969963429122839740L;

	private int no;
	private String bcSeq;
	private String bSeq;
	private String pSeq;
	private int sort;
	private int depth;
	private Integer uSeq;
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
	private int views;
	private String writer;
	private String etc1;
	private String etc2;
	private String etc3;
	private String etc4;
	private String etc5;
	private String etc6;
	private String width;
	private String height;
	private EpBoardDto board;
	private EpUploadFileDto uploadFile;
	private List<EpUploadFileListDto> uploadFileLists;
	private List<EpBoardContentTargetDto> boardContentTargets;

	//variables for file upload
	private List<MultipartFile> files;
	private List<String> uflSeqs;

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

	//Return only date
	public String getDisplayRegDate2()
	{
		return CmDateFormatUtils.format(regDate, null, CmDateFormatUtils.getDateFormat());
	}
}
