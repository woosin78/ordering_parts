package org.jwebppy.platform.mgmt.download.dto;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString@SuperBuilder
@NoArgsConstructor
public class DownloadFileHistoryDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = -5751932931304929945L;

	private Integer dfhSeq;
	private String uflSeq;
	private Integer useq;
	private String originName;
	private String savedName;
	private String path;
}
