package org.jwebppy.platform.mgmt.upload.entity;

import java.util.List;

import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;
import org.springframework.web.multipart.MultipartFile;

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
public class UploadFileListEntity extends MgmtGeneralEntity
{
	private static final long serialVersionUID = 2929207940446247708L;

	private String uflSeq;
	private String ufSeq;
	private String tseq;
	private String originName;
	private String savedName;
	private String extension;
	private long fileSize;
	private String fgDelete;
	private List<MultipartFile> multipartFiles;
}
