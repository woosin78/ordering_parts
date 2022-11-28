package org.jwebppy.portal.common.util;

import java.util.Date;
import java.util.Map;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.StringTransformer;

public class MapFormatOperation implements FormatOperation
{
	private Map<String, Object> map;

	public MapFormatOperation(Map<String, Object> map)
	{
		this.map = map;
	}

	public MapFormatOperation() {}

	public void setTarget(Map<String, Object> map)
	{
		this.map = map;
	}

	@Override
	public void dateFormat(String key) {
		map.put(key, Formatter.getDefDateFormat(map.get(key)));
	}

	@Override
	public void dateFormat(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			dateFormat(keys[i]);
		}
	}

	@Override
	public void notNull(String key)
	{
		map.put(key, CmStringUtils.trimToEmpty(map.get(key)));
	}

	@Override
	public void notNull(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			notNull(keys[i]);
		}
	}

	@Override
	public void deleteFrontZero(String key) {
		map.put(key, Formatter.deleteFrontZero(map.get(key)));
	}

	@Override
	public void deleteFrontZero(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			deleteFrontZero(keys[i]);
		}
	}

	/**
	 * @see com.bpnr.portal.util.Formatter.getDateFormat(String val, String format)
	 */
	@Override
	public void dateFormat(String key, String format)
	{
		Object value = map.get(key);

		if (value != null)
		{
			if (value instanceof Date)
			{
				map.put(key, Formatter.getDateFormat((Date)value, format));
			}
			else
			{
				map.put(key, Formatter.getDateFormat((String)map.get(key), format));
			}
		}
	}

	@Override
	public void dateFormat(String[] keys, String format) {
		for (int i=0; i < keys.length; i++) {
			dateFormat(keys[i], format);
		}
	}

	@Override
	public void decimalFormat(String key) {
		map.put(key, Formatter.fixedPoint(Formatter.getDefDecimalFormat(map.get(key))));
	}

	@Override
	public void decimalFormat(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			decimalFormat(keys[i]);
		}
	}

	@Override
	public void decimalFormat(String key, String format) {
		map.put(key, Formatter.fixedPoint(Formatter.getDecimalFormat(map.get(key).toString(), format)));
	}

	@Override
	public void decimalFormat(String[] keys, String format) {
		for (int i=0; i < keys.length; i++) {
			decimalFormat(keys[i], format);
		}
	}

	@Override
	public void integerFormat(String key) {
		map.put(key, Formatter.getIntegerFormat(map.get(key)));
	}

	@Override
	public void integerFormat(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			integerFormat(keys[i]);
		}
	}

	@Override
	public void priceFormat(String key, String format) {
		map.put(key, Formatter.changePriceFormt((String)map.get(key), format));
	}

	@Override
	public void priceFormat(String[] keys, String format) {
		for (int i=0; i < keys.length; i++) {
			priceFormat(keys[i], format);
		}
	}

	@Override
	public void undelemite(String key, String delim) {
		map.put(key, Formatter.getUnDelim2((String)map.get(key), delim));
	}

	@Override
	public void undelemite(String[] keys, String delim) {
		for (int i=0; i < keys.length; i++) {
			undelemite(keys[i], delim);
		}
	}

	@Override
	public void decimal(String key) {
		map.put(key, Formatter.getDecimal((String)map.get(key)));
	}

	@Override
	public void decimal(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			decimal(keys[i]);
		}
	}

	@Override
	public void getZero(String key) {
		map.put(key, Formatter.getZero((String)map.get(key)));
	}

	@Override
	public void getZero(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			getZero(keys[i]);
		}
	}

	@Override
	public void fillZero(String key, int length) {
		map.put(key, Formatter.fillZero((String)map.get(key), length));
	}

	@Override
	public void fillZero(String[] keys, int length) {
		for (int i=0; i < keys.length; i++) {
			fillZero(keys[i], length);
		}
	}

	@Override
	public void fixedPoint(String key) {
		map.put(key, Formatter.fixedPoint((String)map.get(key)));
	}

	@Override
	public void fixedPoint(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			fixedPoint(keys[i]);
		}
	}

	@Override
	public void ellipsis(String key, int length) {
		map.put(key, Formatter.ellipsis((String)map.get(key), length));
	}

	@Override
	public void ellipsis(String[] keys, int length) {
		for (int i=0; i < keys.length; i++) {
			ellipsis(keys[i], length);
		}
	}

	@Override
	public void apply(String key, StringTransformer stringTransformer) {
		map.put(key, stringTransformer.apply((String)map.get(key)));
	}

	@Override
	public void apply(String[] keys, StringTransformer stringTransformer) {
		for (int i=0; i < keys.length; i++) {
			apply(keys[i], stringTransformer);
		}
	}

	@Override
	public void deleteZeroDate(String key) {
		map.put(key, Formatter.deleteZeroDate((String)map.get(key)));
	}

	@Override
	public void deleteZeroDate(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			deleteZeroDate(keys[i]);
		}
	}

	@Override
	public void viewCode(String key)
	{
		try
		{
			map.put(key, Integer.parseInt((String)map.get(key)));
		}
		catch (NumberFormatException e) {}
	}

	@Override
	public void viewCode(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			viewCode(keys[i]);
		}
	}

	@Override
	public void apply(MapTransformer mapTransformer) {
		mapTransformer.apply(map);
	}

	@Override
	public void getDateFormat(String key) {
		map.put(key, Formatter.getDateFormat((String)map.get(key),"yyyy.MM.dd"));
	}

	@Override
	public void getDateFormat(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			getDateFormat(keys[i]);
		}
	}

	@Override
	public void weightFormat(String key) {
		map.put(key, Formatter.fixedPoint(Formatter.getDecimalFormat(map.get(key).toString(), Formatter.defaultWeightFormat)));
	}

	@Override
	public void weightFormat(String[] keys) {
		for (int i=0; i < keys.length; i++) {
			weightFormat(keys[i]);
		}
	}
}
