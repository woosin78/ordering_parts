package org.jwebppy.portal.iv.board.entity;

import org.jwebppy.portal.iv.common.entity.IvGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpBoardContentTargetEntity extends IvGeneralEntity
{
	private static final long serialVersionUID = -3306974291174273460L;

	private String bcSeq;
	private String code;
	private String description;
	private String type;
}
