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

	private Integer cseq;
	private Integer pseq;
	private Integer useq;
	private Integer lseq;
	private String component;
	private String basename;
	private String fgVisible;
	private String username;
	private String name;
	private String[] names;
	private CItemType type;
	private CItemType[] types;
	private List<Integer> cseqs;
	private String entryPoint;
	private String lang;
	private String query;
	private String fgShowGroup;
}
