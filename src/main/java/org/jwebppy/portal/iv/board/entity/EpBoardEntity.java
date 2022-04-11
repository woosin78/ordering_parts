package org.jwebppy.portal.iv.board.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpBoardEntity extends GeneralEntity
{
	private static final long serialVersionUID = -5090253475844881449L;

	private String bSeq;
	private String name;
	private String description;
	private String fgUseReply;
	private String fgUseComment;
	private String fgSetPeriod;
	private String fgSetTarget;
	private String corp;
	//private String ufSeq;
	private EpUploadFileEntity uploadFile;
}
