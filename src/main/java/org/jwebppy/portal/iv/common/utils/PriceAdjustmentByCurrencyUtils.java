package org.jwebppy.portal.iv.common.utils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.util.CmStringUtils;

public class PriceAdjustmentByCurrencyUtils
{
	public static void calcPriceByCurrency(Object target, String[] keys, String currKey, String[] targets, double value)
	{
		if (target instanceof List)
		{
			List<Map<String, Object>> list = (List<Map<String, Object>>)target;

			if (CollectionUtils.isNotEmpty(list))
			{
				for (Map<String, Object> map: list)
				{
					calcValueByCurrency(map, keys, currKey, targets, value);
				}
			}

		}
		else if (target instanceof Map)
		{
			calcValueByCurrency((Map<String, Object>)target, keys, currKey, targets, value);
		}
	}

	private static void calcValueByCurrency(Map<String, Object> map, String[] keys, String currKey, String[] targets, double value)
	{
		if (MapUtils.isNotEmpty(map))
		{
			if (CmStringUtils.isEmpty(currKey) || ArrayUtils.contains(targets, CmStringUtils.trimToEmpty(map.get(currKey))))
			{
				Iterator<String> it = map.keySet().iterator();

				while (it.hasNext())
				{
					String key = it.next();

					if (ArrayUtils.contains(keys, key))
					{
						if (CmStringUtils.isNotEmpty(map.get(key)))
						{
							try
							{
								map.put(key, new BigDecimal(CmStringUtils.defaultIfEmpty(map.get(key), "0")).multiply(new BigDecimal(value)));
							}
							catch (NumberFormatException e) {}
						}
					}
				}
			}
		}
	}
}
