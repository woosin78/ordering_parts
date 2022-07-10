package org.jwebppy.platform.mgmt.content.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.mgmt.content.dto.CItemType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CItemEntity extends GeneralEntity
{
	private static final long serialVersionUID = -7996090456386384649L;
	private Integer cSeq;
	private Integer pSeq;
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
	private Integer lSeq;
	private List<Integer> seqs;
	private String depth;
	private int userCount;
}