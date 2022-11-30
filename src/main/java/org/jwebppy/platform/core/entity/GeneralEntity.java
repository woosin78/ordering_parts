package org.jwebppy.platform.core.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class GeneralEntity implements Serializable
{
	private static final long serialVersionUID = 8736258968068994440L;

	protected String fgDelete;
	protected String regUsername;
	protected LocalDateTime regDate;
	protected String modUsername;
	protected LocalDateTime modDate;

	protected int rnum;
	@Builder.Default
	protected int pageNumber = 1;
	@Builder.Default
	protected int rowPerPage = 20;
	protected int totalCount;

	public String getFgDelete()
	{
		return CmStringUtils.defaultString(fgDelete, PlatformCommonVo.NO);
	}

	public String getDisplayRegDate()
	{
		return CmDateFormatUtils.format(regDate);
	}

	public String getDisplayModDate()
	{
		return CmDateFormatUtils.format(modDate);
	}
}
