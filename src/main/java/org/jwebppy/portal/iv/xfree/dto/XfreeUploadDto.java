package org.jwebppy.portal.iv.xfree.dto;

import org.jwebppy.portal.iv.common.dto.IvGeneralDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class XfreeUploadDto extends IvGeneralDto
{
	private static final long serialVersionUID = 6335210316588903887L;

	private String contentType;
	private String dialogType;
	private String savedFileName;
	private String viewerPath;
	private String rootId;
	private String error;
}
