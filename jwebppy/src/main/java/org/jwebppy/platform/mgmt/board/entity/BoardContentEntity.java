package org.jwebppy.platform.mgmt.board.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardContentEntity extends GeneralEntity
{
	private static final long serialVersionUID = 3571593885122638421L;

	private Integer bcSeq;
	private Integer bSeq;
	private Integer uSeq;
	private String title;
	private String htmlContent;
	private String textContent;
	private String viewFrom;
	private String viewTo;
	private int views;
	private String writer;
	private String fgDelete;
}
