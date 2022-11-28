package org.jwebppy.portal.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.util.StringTransformer;

public class ListFormatOperation implements FormatOperation
{
	private List<Map<String, Object>> list;
	private MapFormatOperation mapFormatOperation;

	public ListFormatOperation(List<Map<String, Object>> list)
	{
		this.list = list;
		mapFormatOperation = new MapFormatOperation();
	}

	private void applyEach(String methodName, Class[] paramTypes, Object[] args)
	{
		Method method;

		try
		{
			method = mapFormatOperation.getClass().getMethod(methodName, paramTypes);
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}

		try
		{
			for (Map<String, Object> map : list)
			{
				mapFormatOperation.setTarget(map);
				method.invoke(mapFormatOperation, args);
			}
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void dateFormat(String key) {
		applyEach("dateFormat", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void dateFormat(String[] keys) {
		applyEach("dateFormat", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void notNull(String key) {
		applyEach("notNull", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void notNull(String[] keys) {
		applyEach("notNull", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void deleteFrontZero(String key) {
		applyEach("deleteFrontZero", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void deleteFrontZero(String[] keys) {
		applyEach("deleteFrontZero", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void dateFormat(String key, String format) {
		applyEach("dateFormat", new Class[] { String.class, String.class }, new Object[] { key, format });
	}

	@Override
	public void dateFormat(String[] keys, String format) {
		applyEach("dateFormat", new Class[] { String[].class, String.class }, new Object[] { keys, format });
	}

	@Override
	public void decimalFormat(String key) {
		applyEach("decimalFormat", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void decimalFormat(String[] keys) {
		applyEach("decimalFormat", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void decimalFormat(String key, String format) {
		applyEach("decimalFormat", new Class[] { String.class, String.class }, new Object[] { key, format });
	}

	@Override
	public void decimalFormat(String[] keys, String format) {
		applyEach("decimalFormat", new Class[] { String[].class, String.class }, new Object[] { keys, format });
	}

	@Override
	public void priceFormat(String key, String format) {
		applyEach("priceFormat", new Class[] { String.class, String.class }, new Object[] { key, format });
	}

	@Override
	public void priceFormat(String[] keys, String format) {
		applyEach("priceFormat", new Class[] { String[].class, String.class }, new Object[] { keys, format });
	}

	@Override
	public void undelemite(String key, String delim) {
		applyEach("undelemite", new Class[] { String.class, String.class }, new Object[] { key, delim });
	}

	@Override
	public void undelemite(String[] keys, String delim) {
		applyEach("undelemite", new Class[] { String[].class, String.class }, new Object[] { keys, delim });
	}

	@Override
	public void decimal(String key) {
		applyEach("decimal", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void decimal(String[] keys) {
		applyEach("decimal", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void getZero(String key) {
		applyEach("getZero", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void getZero(String[] keys) {
		applyEach("getZero", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void fillZero(String key, int length) {
		applyEach("fillZero", new Class[] { String.class, Integer.class }, new Object[] { key, new Integer(length) });
	}

	@Override
	public void fillZero(String[] keys, int length) {
		applyEach("fillZero", new Class[] { String[].class, Integer.class }, new Object[] { keys, new Integer(length) });
	}

	@Override
	public void fixedPoint(String key) {
		applyEach("fixedPoint", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void fixedPoint(String[] keys) {
		applyEach("fixedPoint", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void ellipsis(String key, int length) {
		applyEach("ellipsis", new Class[] { String.class, Integer.class }, new Object[] { key, new Integer(length) });
	}

	@Override
	public void ellipsis(String[] keys, int length) {
		applyEach("ellipsis", new Class[] { String[].class, Integer.class }, new Object[] { keys, new Integer(length) });
	}

	@Override
	public void apply(String key, StringTransformer stringTransformer) {
		for (int i=0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			mapFormatOperation.setTarget(map);
			mapFormatOperation.apply(key, stringTransformer);
		}
	}

	@Override
	public void apply(String[] keys, StringTransformer stringTransformer) {
		for (int i=0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			mapFormatOperation.setTarget(map);
			mapFormatOperation.apply(keys, stringTransformer);
		}
	}

	@Override
	public void deleteZeroDate(String key) {
		applyEach("deleteZeroDate", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void deleteZeroDate(String[] keys) {
		applyEach("deleteZeroDate", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void viewCode(String key) {
		applyEach("viewCode", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void viewCode(String[] keys) {
		applyEach("viewCode", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void apply(MapTransformer mapTransformer) {
		for (int i=0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			mapTransformer.apply(map);
		}
	}

	@Override
	public void getDateFormat(String key) {
		applyEach("getDateFormat", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void getDateFormat(String[] keys) {
		applyEach("getDateFormat", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void integerFormat(String key) {
		applyEach("integerFormat", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void integerFormat(String[] keys) {
		applyEach("integerFormat", new Class[] { String[].class }, new Object[] { keys });
	}

	@Override
	public void weightFormat(String key) {
		applyEach("weightFormat", new Class[] { String.class }, new Object[] { key });
	}

	@Override
	public void weightFormat(String[] keys) {
		applyEach("weightFormat", new Class[] { String[].class }, new Object[] { keys });
	}
}
