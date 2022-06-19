package org.jwebppy.platform.mgmt.upload.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UploadFileEntity extends GeneralEntity
{
	private static final long serialVersionUID = -770355399257314505L;

	private String ufSeq;
	private String description;
	private long maxFileSize;
	private String path;
	private String exExtension;
	private String inExtension;
}
