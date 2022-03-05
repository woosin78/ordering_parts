package org.jwebppy.portal.iv.download.entity;

import org.jwebppy.portal.iv.common.entity.IvGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EpDownloadFileHistoryEntity extends IvGeneralEntity
{
	private static final long serialVersionUID = -2586059049620107706L;

	private Integer dfhSeq;
	private String ufSeq;
	private String uflSeq;
	private Integer uSeq;
	private String originName;
	private String savedName;
}
