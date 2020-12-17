package org.jwebppy.platform.mgmt.i18n.dto;

import java.io.Serializable;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangDetailDto extends GeneralDto implements Serializable
{
	private static final long serialVersionUID = -2081475230920493964L;

	private Integer ldSeq;
	private Integer lSeq;
	private Integer lkSeq;
	private String text;
	private String fgDelete = PlatformCommonVo.NO;
}
