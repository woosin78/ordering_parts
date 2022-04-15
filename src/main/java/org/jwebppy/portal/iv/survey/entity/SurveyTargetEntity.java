package org.jwebppy.portal.iv.survey.entity;

import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyTargetEntity extends GeneralEntity
{
	private static final long serialVersionUID = -5090253475844881449L;
	
	private int stSeq;
	private int sSeq;
	private String type;
	private String target;
	private String targetName;
	private List<SurveyTargetEntity> surveyTargets;
}
