package org.jwebppy.platform.core.util;

import java.text.DecimalFormat;

import org.apache.commons.lang3.math.NumberUtils;

public class CmNumberUtils extends NumberUtils
{
	public static String round(double num, String pattern)
	{
		DecimalFormat decimalFormat = new DecimalFormat(pattern);

		return decimalFormat.format(num);
	}

	public static String roundTo2Places(double num)
	{
		return round(num, "#.##");
	}
}
