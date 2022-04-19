package org.jwebppy.portal.iv.survey.entity;

import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyApplyEntity extends GeneralEntity
{
	private static final long serialVersionUID = -5090253475844881449L;
	
	private int saSeq;
	private int sSeq;
	private int sqSeq;
	private int siSeq;
	private Integer uSeq;
	private String title;
	private String type;
	private String answer;
	private int mandatoryCnt;
	private String fgMandatory;
	private String regName;
	private List<SurveyApplyEntity> items;
}
