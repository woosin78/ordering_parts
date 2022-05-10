package org.jwebppy.portal.iv.eu.common.file_upload.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDownloadHistoryDto extends GeneralDto
{
	private Integer fdhSeq;
	private Integer fuSeq;
	
	public FileDownloadHistoryDto() {}	
}
 