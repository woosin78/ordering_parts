package org.jwebppy.platform.mgmt.upload.entity;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class UploadFileEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = -770355399257314505L;

	private String ufSeq;
	private String description;
	private long maxFileSize;
	private String path;
	private String exExtension;
	private String inExtension;
}
