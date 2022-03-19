package org.jwebppy.portal.iv.board.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpBoardDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;

	private String bSeq;
	private String name;
	private String description;
	private String fgUseReply;
	private String fgUseComment;
	private String fgSetPeriod;
	private String fgSetTarget;
	private String corp;
	//private String ufSeq;
	private EpUploadFileDto uploadFile;
}
