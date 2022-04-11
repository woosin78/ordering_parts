package org.jwebppy.portal.iv.download.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EpDownloadFileHistoryDto extends IvGeneralDto
{
	private static final long serialVersionUID = -49605620479232026L;

	private Integer dfhSeq;
	private String ufSeq;
	private String uflSeq;
	private Integer uSeq;
	private String originName;
	private String savedName;
}
