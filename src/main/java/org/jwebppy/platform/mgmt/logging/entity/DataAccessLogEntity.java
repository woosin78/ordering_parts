package org.jwebppy.platform.mgmt.logging.entity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.core.util.CmStringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataAccessLogEntity extends GeneralEntity
{
	private static final long serialVersionUID = -1852102477184581249L;
	private String dlSeq;
	private String type;
	private String command;
	private String className;
	private String methodName;
	private String destination;
	private long startTime;
	private long elapsed;
	private String requestId;
	private String requestUri;
	private String referer;
	private String sessionId;
	private String error;
	private String timezone;
	private List<DataAccessLogParameterEntity> dataAccessLogParameters;

	public String getRefererUri()
	{
		try
		{
			if (CmStringUtils.isNotEmpty(referer))
			{
				return new URL(referer).getPath();
			}
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
