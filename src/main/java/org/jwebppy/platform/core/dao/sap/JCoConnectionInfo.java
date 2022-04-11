package org.jwebppy.platform.core.dao.sap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class JCoConnectionInfo
{
	private String name;
	private String mshost;
	private String r3name;
	private String group;
    private String ashost;
    private String sysnr;
    private String client;
    private String user;
    private String password;
    private String alias;
    private String language;
    private String landscape;
    private String poolCapacity;
	private String peakLimit;
}