package org.jwebppy.portal.iv.hq.parts.domestic.order.create.util;

import org.jwebppy.platform.core.util.CmStringUtils;

public class OrderCreationUtils
{
	public static String makeLineNo(int seq)
	{
		return CmStringUtils.leftPad(seq*10, 6, "0");
	}
}
