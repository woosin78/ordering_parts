package org.jwebppy.portal.iv.survey.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveySearchDto extends IvGeneralDto
{
	private static final long serialVersionUID = -6795655439701455791L;

	private int sSeq;
	private int sqSeq;
	private String fgStatus;
	private String query;
	private Integer uSeq;
}
