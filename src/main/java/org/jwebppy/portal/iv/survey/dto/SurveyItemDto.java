package org.jwebppy.portal.iv.survey.dto;

import java.util.ArrayList;
import java.util.List;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SurveyItemDto extends IvGeneralDto
{
	private static final long serialVersionUID = -7094243416317320896L;

	private int siSeq;
	private int sqSeq;
	private String title;

	private ArrayList<SurveyItemDto> surveyItemList;
}
