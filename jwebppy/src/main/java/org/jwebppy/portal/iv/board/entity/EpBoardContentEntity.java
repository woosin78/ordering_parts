package org.jwebppy.portal.iv.board.entity;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpBoardContentEntity extends GeneralEntity
{
	private static final long serialVersionUID = 3571593885122638421L;

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
}
