package org.jwebppy.platform.mgmt.upload.entity;

import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UploadFileListEntity extends GeneralEntity
{
	private static final long serialVersionUID = 2929207940446247708L;

	private String uflSeq;
	private String ufSeq;
	private String tSeq;
	private String originName;
	private String savedName;
	private String extension;
	private long fileSize;
	private String fgDelete;
	private List<MultipartFile> multipartFiles;
}
