package org.jwebppy.portal.iv.survey.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyTargetDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;
	
	private int stSeq;
	private int sSeq;
	private String type;
	private String target;
	private String targetName;
	
}
