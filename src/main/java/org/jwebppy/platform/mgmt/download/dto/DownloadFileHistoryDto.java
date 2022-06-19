package org.jwebppy.platform.mgmt.download.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DownloadFileHistoryDto extends GeneralDto
{
	private static final long serialVersionUID = -5751932931304929945L;

	private Integer dfhSeq;
	private String ufSeq;
	private String uflSeq;
	private Integer uSeq;
	private String originName;
	private String savedName;
}
