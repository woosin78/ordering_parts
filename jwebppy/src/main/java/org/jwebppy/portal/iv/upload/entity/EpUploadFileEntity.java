package org.jwebppy.portal.iv.upload.entity;

import org.jwebppy.portal.iv.common.entity.IvGeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpUploadFileEntity extends IvGeneralEntity
{
	private static final long serialVersionUID = -770355399257314505L;

	private String ufSeq;
	private String description;
	private long maxFileSize;
	private String path;
	private String exExtension;
	private String inExtension;
}
