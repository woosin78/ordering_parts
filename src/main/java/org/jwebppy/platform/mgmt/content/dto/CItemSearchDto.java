package org.jwebppy.platform.mgmt.content.dto;

import java.util.List;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class CItemSearchDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 8595296313056450072L;

	private Integer cSeq;
	private Integer pSeq;
	private Integer uSeq;
	private Integer lSeq;
	private String component;
	private String basename;
	private String fgVisible;
	private String username;
	private String name;
	private String[] names;
	private CItemType type;
	private CItemType[] types;
	private List<Integer> cSeqs;
	private String entryPoint;
	private String lang;
	private String query;
	private String fgShowGroup;
}
