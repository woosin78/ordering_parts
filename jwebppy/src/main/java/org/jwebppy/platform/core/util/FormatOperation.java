package org.jwebppy.platform.core.util;

public interface FormatOperation
{
	public void dateFormat(String key);

	public void dateFormat(String[] keys);

	public void notNull(String key);

	public void notNull(String[] keys);

	public void deleteFrontZero(String key);

	public void deleteFrontZero(String[] keys);

	public void dateFormat(String key, String format);

	public void dateFormat(String[] keys, String format);

	public void decimalFormat(String key);

	public void decimalFormat(String[] keys);

	public void decimalFormat(String key, String format);

	public void decimalFormat(String[] keys, String format);

	public void integerFormat(String key);

	public void integerFormat(String[] keys);

	public void priceFormat(String key, String format);

	public void priceFormat(String[] keys, String format);

	public void undelemite(String key, String delim);

	public void undelemite(String[] keys, String delim);

	public void decimal(String key);

	public void decimal(String[] keys);

	public void getZero(String key);

	public void getZero(String[] keys);

	public void fillZero(String key, int length);

	public void fillZero(String[] keys, int length);

	public void fixedPoint(String key);

	public void fixedPoint(String[] keys);

	public void ellipsis(String key, int length);

	public void ellipsis(String[] keys, int length);

	public void apply(String key, StringTransformer stringTransformer);

	public void apply(String[] keys, StringTransformer stringTransformer);

	public void deleteZeroDate(String key);

	public void deleteZeroDate(String[] key);

	public void viewCode(String key);

	public void viewCode(String[] key);

	public void apply(MapTransformer mapTransformer);

	public void getDateFormat(String key);

	public void getDateFormat(String[] key);

	public void weightFormat(String key);

	public void weightFormat(String[] key);
}
