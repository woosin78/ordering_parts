package org.jwebppy.portal.iv.eu.common.file_upload.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileDownloadHistoryEntity extends GeneralEntity
{
	private static final long serialVersionUID = -260725984184207386L;

	private Integer fdhSeq;
	private String fuSeq;

	public FileDownloadHistoryEntity() {}
}
