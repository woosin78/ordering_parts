package org.jwebppy.portal.iv.hq.parts.export.order.create.util;

import org.jwebppy.platform.core.util.CmStringUtils;

public class ExOrderCreationUtils
{
	public static String lineNo(int seq)
	{
		return CmStringUtils.leftPad(seq*10, 6, "0");
	}
}
