package org.jwebppy.platform.mgmt.upload_file.entity;

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

	private Integer uflSeq;
	private Integer ufSeq;
	private Integer tSeq;
	private String originName;
	private String savedName;
	private String extension;
	private long size;
	private String fgDelete;
	private List<MultipartFile> multipartFiles;
}
