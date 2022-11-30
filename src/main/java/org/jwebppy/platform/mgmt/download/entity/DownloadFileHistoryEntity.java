package org.jwebppy.platform.mgmt.download.entity;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

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
public class DownloadFileHistoryEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = -4957969853711359643L;

	private Integer dfhSeq;
	private String uflSeq;
	private Integer uSeq;
	private String originName;
	private String savedName;
	private String path;
}
