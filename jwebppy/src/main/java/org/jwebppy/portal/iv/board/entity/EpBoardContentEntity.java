package org.jwebppy.portal.iv.board.entity;

import java.time.LocalDateTime;
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
public class EpBoardContentEntity extends GeneralEntity
{
	private static final long serialVersionUID = 3571593885122638421L;

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
	private int views;
	private String writer;
	private String etc1;
	private String etc2;
	private String etc3;
	private String etc4;
	private String etc5;
	private String etc6;
	private EpBoardEntity board;
	private EpUploadFileEntity fileUploadFile;
	private List<EpUploadFileListEntity> fileUploadFileLists;
}
