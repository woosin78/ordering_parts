package org.jwebppy.platform.mgmt.upload_file.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UploadFileListDto extends GeneralDto
{
	private static final long serialVersionUID = 7896823726474538055L;

	private Integer uflSeq;
	private Integer ufSeq;
	private Integer tSeq;
	private String originName;
	private String savedName;
	private String extension;
	private long size;
	private String fgDelete;
}
