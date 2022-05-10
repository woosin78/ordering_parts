package org.jwebppy.portal.iv.eu.common.file_upload.entity;

import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileUploadEntity extends GeneralEntity
{
	private static final long serialVersionUID = -8195343547130977045L;

	private Integer fuSeq;
	private String target;
	private Integer tSeq;
	private String originName;
	private String savedName;
	private String extension;
	private Integer fileSize;
	private String storagePath;
	private String fgDelete;
	private String type;
	private String changeFileSize;
	private String fgFileStatus;

	private List<String> fgFileStatuss;	//I:등록,D:삭제
	private List<Integer> fuSeqs;
	private List<String> fileNames;

	private String bbsCorp;
	private String userCorp;

	public FileUploadEntity() {}
}
