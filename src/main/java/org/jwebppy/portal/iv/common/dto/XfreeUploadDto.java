package org.jwebppy.portal.iv.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class XfreeUploadDto extends IvGeneralDto
{
	private static final long serialVersionUID = 6335210316588903887L;

	private String contentType;
	private String uploadedPath;
	private String dialogType;
	private String errorLog;
	private String rootId;
	private String saveFilePath;
	private String basePath;
}
