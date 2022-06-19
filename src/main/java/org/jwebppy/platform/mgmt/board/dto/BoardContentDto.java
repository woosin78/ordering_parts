package org.jwebppy.platform.mgmt.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardContentDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -1969963429122839740L;

	private Integer bcSeq;
	private Integer bSeq;
	private Integer pSeq;
	private int sort;
	private int depth;
	private Integer uSeq;
	private String title;
	private String htmlContent;
	private String textContent;
	private LocalDateTime fromView;
	private LocalDateTime toView;
	private int views;
	private String writer;

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
}
