package org.jwebppy.platform.mgmt.conn_resource.entity;

import java.util.List;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.entity.MgmtGeneralEntity;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnType;
import org.jwebppy.platform.mgmt.user.entity.UserGroupEntity;

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
public class SapConnResourceEntity extends MgmtGeneralEntity implements IPagination
{
	private static final long serialVersionUID = -5876202081784725634L;

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
	private List<UserGroupEntity> userGroups;

	public SapConnResourceEntity(Integer scrSeq)
	{
		this.scrSeq = scrSeq;
	}
}
