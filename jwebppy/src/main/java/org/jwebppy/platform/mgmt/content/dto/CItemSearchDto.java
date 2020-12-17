package org.jwebppy.platform.mgmt.content.dto;

import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemSearchDto extends GeneralDto
{
	private Integer cSeq;
	private Integer pSeq;
	private Integer uSeq;
	private Integer lSeq;
	private String component;
	private String basename;
	private String fgVisible;
	private String username;
	private String name;
	private String type;
	private String[] types;
	private List<Integer> cSeqs;
	private String entryPoint;
	private String lang;
}
