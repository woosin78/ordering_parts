package org.jwebppy.portal.iv.upload.entity;

import java.util.List;

import org.jwebppy.portal.iv.common.entity.IvGeneralEntity;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpUploadFileListEntity extends IvGeneralEntity
{
	private static final long serialVersionUID = 2929207940446247708L;

	private String uflSeq;
	private String ufSeq;
	private String tSeq;
	private String originName;
	private String savedName;
	private String extension;
	private long size;
	private List<MultipartFile> multipartFiles;
}
