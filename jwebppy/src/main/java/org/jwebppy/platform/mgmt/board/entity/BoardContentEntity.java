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
	private String content;
	private String fgViewFrom;
	private String fgViewTo;
	private String fgDelete;
}
