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
	private static final long serialVersionUID = 8595296313056450072L;

	private Integer cSeq;
	private Integer pSeq;
	private Integer uSeq;
	private Integer lSeq;
	private String component;
	private String basename;
	private String fgVisible;
	private String fgDelete;
	private String username;
	private String name;
	private CItemType type;
	private CItemType[] types;
	private List<Integer> cSeqs;
	private String entryPoint;
	private String lang;
	private String query;
	private String fgShowGroup;
}
