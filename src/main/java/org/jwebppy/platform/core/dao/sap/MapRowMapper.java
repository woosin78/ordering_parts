package org.jwebppy.platform.core.dao.sap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sapportals.connector.ConnectorException;
import com.sapportals.connector.execution.structures.IAbstractRecord;
import com.sapportals.connector.execution.structures.IRecordMetaData;

public class MapRowMapper implements RowMapper
{
    private String[] outColumns;

    public MapRowMapper() {}

    public MapRowMapper(String[] outColumns)
    {
        this.outColumns = outColumns;
    }

    public void setOutputColumns(String[] outColumns)
    {
        this.outColumns = outColumns;
    }

    public String[] getOutputColumns()
    {
        return this.outColumns;
    }

    public Map mapRow(Object rs)
    {
        try
        {
            IAbstractRecord recordSet = (IAbstractRecord)rs;

            Map rowMap = new HashMap();

            IRecordMetaData rmd = recordSet.retrieveMetaData();
            String columnName = null;
            Object value = null;

            for (int i=0, cnt=rmd.getColumnCount(); i<cnt; i++)
            {
                columnName = recordSet.getColumnName(i);

                if (isOutputColumn(columnName))
                {
                    try
                    {
                        value = recordSet.getObject(i);
                    }
                    catch (Exception e)//com.sap.mw.jco.JCO$ConversionException
                    {
                        value = recordSet.getString(i);
                    }

                    rowMap.put(columnName, value);
                }
            }

            return rowMap;
        }
        catch (ConnectorException e)
        {
            e.printStackTrace();
        }

        return Collections.EMPTY_MAP;
    }

    public boolean isOutputColumn(String columnName)
    {
        if (outColumns == null || outColumns.length == 0)
        {
            return true;
        }

        for (int i=0, len=outColumns.length; i<len; i++)
        {
            if (outColumns[i].equals(columnName))
            {
                return true;
            }
        }

        return false;
    }
}
