package org.jwebppy.platform.mgmt.upload.dto;

import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

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
public class UploadFileDto extends MgmtGeneralDto
{
	private static final long serialVersionUID = 2354731635588102842L;

	private String ufSeq;
	private String description;
	private long maxFileSize = 0l;
	private String path;
	private String exExtension;
	private String inExtension;
}
