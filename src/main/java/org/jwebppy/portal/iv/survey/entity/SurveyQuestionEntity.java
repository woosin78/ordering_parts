package org.jwebppy.portal.iv.survey.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyQuestionEntity extends GeneralEntity
{
	private static final long serialVersionUID = -5090253475844881449L;
	
	private int sqSeq;
	private int sSeq;
	private String title;
	private String type;
	private int mandatoryCnt;
	private int itemCnt;
	private String fgMandatory;
}
