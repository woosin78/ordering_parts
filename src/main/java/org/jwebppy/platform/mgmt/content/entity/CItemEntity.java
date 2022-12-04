package org.jwebppy.platform.mgmt.content.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;
import org.jwebppy.platform.mgmt.content.dto.CItemType;

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
public class CItemEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = -7996090456386384649L;

	private Integer cseq;
	private Integer pseq;
	private CItemType type;
	private String name;
	private String name2;
	private String description;
	private String component;
	private String entryPoint;
	private String parameter;
	private int sort;
	private String fgVisible;
	private LocalDateTime fromValid;
	private LocalDateTime toValid;
	private String launchType;
	private String scrWidth;
	private String scrHeight;
	private Integer lseq;
	private List<Integer> seqs;
	private String depth;
	private int userCount;
}