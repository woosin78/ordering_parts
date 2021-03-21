package org.jwebppy.platform.core.util;

import java.util.Random;
import java.util.UUID;

public class UidGenerateUtils
{
	public static String generate()
	{
		return Integer.toString(new Random().nextInt(2)) + "-" + UUID.randomUUID();
	}
}
