package org.jwebppy.platform.mgmt.upload.dto;

import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UploadFileDto extends GeneralDto
{
	private static final long serialVersionUID = 2354731635588102842L;

	private Integer ufSeq;
	private String description;
	private long maxFileSize = 0l;
	private String path;
	private String exExtension;
	private String inExtension;
}
