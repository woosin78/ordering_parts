package org.jwebppy.platform.mgmt.conn_resource.dto;

import java.util.List;

import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.security.AES256Cipher;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;

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
	private String r3name;
	private String username;
	private String password;
	private String poolCapacity;
	private String peakLimit;
	private String language;
	private String fgUseUserLang;
	private String fgUse;
	private List<UserGroupDto> userGroups;

	public SapConnResourceDto() {}

	public SapConnResourceDto(Integer scrSeq)
	{
		this.scrSeq = scrSeq;
	}

	public SapConnType getType()
	{
		return (type == null) ? SapConnType.C : type;
	}

	public String getFgUse()
	{
		return CmStringUtils.defaultIfEmpty(fgUse, MgmtCommonVo.NO);
	}

	public String getFgUseUserLang()
	{
		return CmStringUtils.defaultIfEmpty(fgUseUserLang, MgmtCommonVo.NO);
	}

	public String getDecodedPassword()
	{
		return (CmStringUtils.isNotEmpty(password)) ? AES256Cipher.getInstance().decode(password) : password;
	}

	public boolean isEmpty()
	{
		return (scrSeq == null);
	}

	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
}
