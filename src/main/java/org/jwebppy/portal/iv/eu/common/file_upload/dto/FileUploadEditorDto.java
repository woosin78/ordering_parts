package org.jwebppy.portal.iv.eu.common.file_upload.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileUploadEditorDto extends GeneralDto
{
	private String contentType;
	private String uploadedPath;
	private String dialogType;
	private String errorLog;
	private String rootId;
	private String saveFilePath;
	private String basePath;

	public FileUploadEditorDto() {}
}