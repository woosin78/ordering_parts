package org.jwebppy.portal.iv.upload.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EpUploadFileDto extends IvGeneralDto
{
	private static final long serialVersionUID = 2354731635588102842L;

	private String ufSeq;
	private String description;
	@Builder.Default
	private long maxFileSize = 0l;
	private String path;
	private String exExtension;
	private String inExtension;
}
