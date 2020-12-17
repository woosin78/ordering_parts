package org.jwebppy.platform.core.dao.sap;

public class RfcDataAccessException extends RuntimeException
{
	private static final long serialVersionUID = 5209606873096135371L;

	private final int group;

	public RfcDataAccessException(int group, String message)
	{
		super(message);
		this.group = group;
	}

	public RfcDataAccessException(int group, String message, Throwable cause)
	{
		super(message, cause);
		this.group = group;
	}

	public int getGroup()
	{
		return group;
	}
}
