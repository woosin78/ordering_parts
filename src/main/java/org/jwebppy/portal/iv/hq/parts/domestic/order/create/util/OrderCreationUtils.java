package org.jwebppy.portal.iv.hq.parts.domestic.order.create.util;

import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.common.PortalCommonVo;

public class OrderCreationUtils
{
	public static String lineNo(int seq)
	{
		return CmStringUtils.leftPad(seq*10, 6, "0");
	}

	public static String poNo(String customerNo)
	{
		return CmStringUtils.stripStart(customerNo, "0") + CmDateFormatUtils.format(CmDateTimeUtils.now(), PortalCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS);
	}
}
