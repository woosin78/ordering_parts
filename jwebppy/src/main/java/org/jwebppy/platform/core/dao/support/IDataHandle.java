package org.jwebppy.platform.core.dao.support;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public interface IDataHandle
{
    public String getString(Object name);
    public String getString(Object name, String defaultValue);
    public byte[] getBytes(Object name);
    public int getInt(Object name);
    public int getInt(Object name, int defaultValue);
    public long getLong(Object name);
    public long getLong(Object name, long defaultValue);
    public float getFloat(Object name);
    public float getFloat(Object name, float defaultValue);
    public double getDouble(Object name);
    public double getDouble(Object name, double defaultValue);
    public BigDecimal getDecimal(Object name);
    public BigDecimal getDecimal(Object name, BigDecimal defaultValue);
    public Date getDate(Object name);
    public Time getTime(Object name);
    public Timestamp getTimestamp(Object name);
    public Object getObject(Object name);
    public boolean isNullValue(Object name);
    public boolean isEmptyValue(Object name);
    public boolean isNotEmptyValue(Object name);
    public boolean isEquals(Object name, Object value);
}
