package org.jwebppy.platform.core.util;

import java.util.List;
import java.util.Map;

public class FormatBuilder
{
	private final FormatOperation formatOperation;

	/**
	 * 작업할 대상 객체를 지정함. 대상 객체는 List이거나 Map 타입이어야 한다.
	 *
	 * @param target
	 * @return
	 */
	public static FormatBuilder with(Object target)
	{
		return new FormatBuilder(target);
	}

	private FormatBuilder(Object target)
	{
		if (target instanceof Map)
		{
			this.formatOperation = new MapFormatOperation((Map)target);
		}
		else if (target instanceof List)
		{
			this.formatOperation = new ListFormatOperation((List)target);
		}
		else
		{
			throw new UnsupportedOperationException("Only Map and List type are supported");
		}
	}

	// ############### Formatter method.
	public FormatBuilder dateFormat(String key) {
		formatOperation.dateFormat(key);
		return this;
	}

	public FormatBuilder dateFormat(String[] keys) {
		formatOperation.dateFormat(keys);
		return this;
	}

	public FormatBuilder dateFormat(String key, String format) {
		formatOperation.dateFormat(key, format);
		return this;
	}

	public FormatBuilder dateFormat(String[] keys, String format) {
		formatOperation.dateFormat(keys, format);
		return this;
	}

	public FormatBuilder qtyFormat(String key) {
		formatOperation.decimalFormat(key, "#,###");
		return this;
	}

	public FormatBuilder qtyFormat(String[] keys) {
		formatOperation.decimalFormat(keys, "#,###");
		return this;
	}

	public FormatBuilder decimalFormat(String key) {
		formatOperation.decimalFormat(key);
		return this;
	}

	public FormatBuilder decimalFormat(String[] keys) {
		formatOperation.decimalFormat(keys);
		return this;
	}

	public FormatBuilder decimalFormat(String key, String format) {
		formatOperation.decimalFormat(key, format);
		return this;
	}

	public FormatBuilder decimalFormat(String[] keys, String format) {
		formatOperation.decimalFormat(keys, format);
		return this;
	}

	public FormatBuilder integerFormat(String key) {
		formatOperation.integerFormat(key);
		return this;
	}

	public FormatBuilder integerFormat(String[] keys) {
		formatOperation.integerFormat(keys);
		return this;
	}

	public FormatBuilder priceFormt(String key, String format) {
		formatOperation.priceFormat(key, format);
		return this;
	}

	public FormatBuilder priceFormt(String[] keys, String format) {
		formatOperation.priceFormat(keys, format);
		return this;
	}

	public FormatBuilder undelimite(String key, String delim) {
		formatOperation.undelemite(key, delim);
		return this;
	}

	public FormatBuilder undelimite(String[] keys, String delim) {
		formatOperation.undelemite(keys, delim);
		return this;
	}

	public FormatBuilder decimal(String key) {
		formatOperation.decimal(key);
		return this;
	}

	public FormatBuilder decimal(String[] keys) {
		formatOperation.decimal(keys);
		return this;
	}

	public FormatBuilder getZero(String key) {
		formatOperation.getZero(key);
		return this;
	}

	public FormatBuilder getZero(String[] keys) {
		formatOperation.getZero(keys);
		return this;
	}

	public FormatBuilder fillZero(String key, int length) {
		formatOperation.fillZero(key, length);
		return this;
	}

	public FormatBuilder fillZero(String[] keys, int length) {
		formatOperation.fillZero(keys, length);
		return this;
	}

	public FormatBuilder fixedPoint(String key) {
		formatOperation.fixedPoint(key);
		return this;
	}

	public FormatBuilder fixedPoint(String[] keys) {
		formatOperation.fixedPoint(keys);
		return this;
	}

	public FormatBuilder deleteFrontZero(String key) {
		formatOperation.deleteFrontZero(key);
		return this;
	}

	public FormatBuilder deleteFrontZero(String[] keys) {
		formatOperation.deleteFrontZero(keys);
		return this;
	}

	public FormatBuilder ellipsis(String key, int length) {
		formatOperation.ellipsis(key, length);
		return this;
	}

	public FormatBuilder ellipsis(String[] keys, int length) {
		formatOperation.ellipsis(keys, length);
		return this;
	}

	// ########### StringTrans methods.
	public FormatBuilder notNull(String key) {
		formatOperation.notNull(key);
		return this;
	}

	public FormatBuilder notNull(String[] keys) {
		formatOperation.notNull(keys);
		return this;
	}

	/**
	 *
	 * @param key
	 * @param stringTransformer
	 * @return
	 */
	public FormatBuilder apply(String key, StringTransformer stringTransformer) {
		formatOperation.apply(key, stringTransformer);
		return this;
	}

	public FormatBuilder apply(String[] keys, StringTransformer stringTransformer) {
		formatOperation.apply(keys, stringTransformer);
		return this;
	}

	public FormatBuilder apply(MapTransformer mapTransformer) {
		formatOperation.apply(mapTransformer);
		return this;
	}

	public FormatBuilder deleteZeroDate(String key) {
		formatOperation.deleteZeroDate(key);
		return this;
	}

	public FormatBuilder deleteZeroDate(String[] key) {
		formatOperation.deleteZeroDate(key);
		return this;
	}

	/**
	 * 정수앞에 0을 지우기
	 * @param key
	 * @return
	 */
	public FormatBuilder viewCode(String key){
		formatOperation.viewCode(key);
		return this;
	}

	public FormatBuilder viewCode(String[] key){
		formatOperation.viewCode(key);
		return this;
	}

	public FormatBuilder getDateFormat(String key){
		formatOperation.getDateFormat(key);
		return this;
	}

	public FormatBuilder getDateFormat(String[] key){
		formatOperation.getDateFormat(key);
		return this;
	}
}
