package org.jwebppy.portal.iv.common.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.Formatter;

public class PriceAdjustmentByCurrencyUtils
{
	public static void calcPriceByCurrency(Object target, String[] keys, String currKey, String[] targets, double value)
	{
		if (ArrayUtils.isNotEmpty(keys))
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

	public static Map<String, Double> sum(List list, String[] keys)
	{
		Map<String, Double> returnMap = new HashMap<>();

		try
		{
			if (CollectionUtils.isNotEmpty(list) && ArrayUtils.isNotEmpty(keys))
			{
				Double[] amounts = new Double[keys.length];

				for (int i=0, length=keys.length; i<length; i++)
				{
					amounts[i] = 0.0;
				}

				for (int i=0, size=list.size(); i<size; i++)
				{
					Map map = (Map)list.get(i);

					for (int j=0, length=keys.length; j<length; j++)
					{
						Object value = map.get(keys[j]);

						if (ObjectUtils.isEmpty(value))
						{
							continue;
						}

						amounts[j] += Formatter.getUnFormattedValue(value.toString());
					}
				}

				for (int i=0, length=keys.length; i<length; i++)
				{
					returnMap.put("_SUM_" + keys[i], amounts[i]);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return returnMap;
	}
}
