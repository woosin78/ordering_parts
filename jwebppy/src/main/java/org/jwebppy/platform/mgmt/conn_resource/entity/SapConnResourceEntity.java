package org.jwebppy.platform.mgmt.conn_resource.entity;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SapConnResourceEntity extends GeneralEntity implements IPagination
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
	private String username;
	private String password;
	private String poolCapacity;
	private String peakLimit;
	private String language;
	private String fgUseUserLang;
	private String fgUse;
}
