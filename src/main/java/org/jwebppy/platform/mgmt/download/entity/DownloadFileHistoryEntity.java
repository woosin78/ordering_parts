package org.jwebppy.platform.mgmt.download.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DownloadFileHistoryEntity extends GeneralEntity
{
	private static final long serialVersionUID = -4957969853711359643L;

	private Integer dfhSeq;
	private Integer ufSeq;
	private Integer uflSeq;
	private Integer uSeq;
	private String originName;
	private String savedName;
}
