package org.jwebppy.portal.iv.eu.common.file_upload.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileUploadInfoDto extends GeneralDto
{
	private String fuiId;
	private String fileStoragePath;
	private Long fileLimitSize;
	private String fileAllowExt;
	private String fgDelete;

	public FileUploadInfoDto() {}
}
