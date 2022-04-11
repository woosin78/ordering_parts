package org.jwebppy.platform.mgmt.board.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardEntity extends GeneralEntity
{
	private static final long serialVersionUID = -5090253475844881449L;

	private Integer bSeq;
	private String bId;
	private String description;
	private String fgUseReply;
	private String fgUseComment;
	private String fgSetPeriod;
	private Integer ufSeq;
}
