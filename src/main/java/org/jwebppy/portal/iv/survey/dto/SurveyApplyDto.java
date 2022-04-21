package org.jwebppy.portal.iv.survey.dto;

import java.util.List;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyApplyDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;

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
	private int voteCnt;
	private String resultTxt;
	private List<SurveyApplyDto> items;
}
