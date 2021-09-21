package org.jwebppy.platform.mgmt.conn_resource.dto;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SapConnResourceDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = 3420212280699526566L;

	private Integer scrSeq;
	private SapConnType type;
	private String name;
	private String description;
	private String appServer;
	private String instanceNo;
	private String systemId;
	private String routerString;
	private String msgServer;
	private String router;
	private String grpServer;
	private String client;
	private String username;
	private String password;
	private String poolCapacity;
	private String peakLimit;
	private String language;
	private String fgUseUserLang;
	private String fgUse;

	public SapConnType getType()
	{
		if (type == null)
		{
			return SapConnType.C;
		}

		return type;
	}

	public String getFgUse()
	{
		return CmStringUtils.defaultIfEmpty(fgUse, PlatformCommonVo.NO);
	}
}
