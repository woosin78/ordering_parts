package org.jwebppy.portal.iv.eu.common.file_upload.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileUploadInfoEntity extends GeneralEntity
{
	private static final long serialVersionUID = -1831949667820014858L;

	private String fuiId;
	private String fileStoragePath;
	private Integer fileLimitSize;
	private String fileAllowExt;
	private String fgDelete;

	public FileUploadInfoEntity() {}
}
