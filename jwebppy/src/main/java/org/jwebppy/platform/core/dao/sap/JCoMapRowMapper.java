package org.jwebppy.platform.core.dao.sap;

import java.util.LinkedHashMap;
import java.util.Map;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoRecordFieldIterator;

public class JCoMapRowMapper extends MapRowMapper
{
    @Override
	public Map<String, Object> mapRow(Object rs)
    {
        JCoRecordFieldIterator jCoRecordFieldIterator = (JCoRecordFieldIterator)rs;

        JCoField jCoField = null;
        String fieldName = null;
        Object value = null;
        Map<String, Object> rowMap = new LinkedHashMap<>();

        while (jCoRecordFieldIterator.hasNextField())
        {
            jCoField = jCoRecordFieldIterator.nextField();

            fieldName = jCoField.getName();

            if (isOutputColumn(fieldName))
            {
                try
                {
                    value = jCoField.getValue();
                }
                catch (Exception e)
                {
                    value = jCoField.getString();
                }

                //if (value != null)
                {
                    rowMap.put(fieldName, value);
                }
            }
        }

        return rowMap;
    }
}
