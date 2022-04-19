package org.jwebppy.portal.iv.survey.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyQuestionDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;

	private int sqSeq;
	private int sSeq;
	private String title;
	private String type;
	private int mandatoryCnt;
	private int itemCnt;
	private String fgMandatory;
}
