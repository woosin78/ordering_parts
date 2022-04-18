package org.jwebppy.portal.iv.survey.dto;

import java.util.List;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyVoteDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;

	private int sSeq;
	private int sqSeq;
	private int siSeq;
	private String title;
	private String type;
	private int mandatoryCnt;
	private String fgMandatory;
	private List<SurveyVoteDto> items;
}
