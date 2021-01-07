package org.jwebppy.platform.core.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jwebppy.platform.core.util.CmDateFormatUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GeneralEntity implements Serializable
{
	private static final long serialVersionUID = 8736258968068994440L;

	protected String regUsername;
	protected LocalDateTime regDate;
	protected String modUsername;
	protected LocalDateTime modDate;

	protected int rnum = 0;
	protected int pageNumber = 1;
	protected int rowPerPage = 20;
	protected int totalCount = 0;

	public String getDisplayRegDate()
	{
		return CmDateFormatUtils.format(regDate);
	}

	public String getDisplayModDate()
	{
		return CmDateFormatUtils.format(modDate);
	}
}
