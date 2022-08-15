package org.jwebppy.platform.core.dao.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.jwebppy.platform.core.dao.AbstractDaoTemplate;
import org.jwebppy.platform.core.dao.IDaoRequest;
import org.jwebppy.platform.core.dao.MapParameterSource;
import org.jwebppy.platform.core.dao.ParameterValue;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.MyLocalLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoMetaData;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class RfcTemplate extends AbstractDaoTemplate
{
	private Logger logger = LoggerFactory.getLogger(RfcTemplate.class);

    private JCoConnectionResource jCoConnectionResource;

    private RowMapper DEFAULT_ROW_MAPPER = new JCoMapRowMapper();

    public RfcTemplate() {}

    public RfcTemplate(JCoConnectionResource jCoConnectionResource)
    {
    	setConnectionResource(jCoConnectionResource);
    }

    public void setConnectionResource(JCoConnectionResource jCoConnectionResource)
    {
        this.jCoConnectionResource = jCoConnectionResource;
    }

    public JCoConnectionResource getConnectionResource()
    {
    	return this.jCoConnectionResource;
    }

    private String getLandscape(RfcRequest rfcRequest)
    {
    	return CmStringUtils.defaultString(rfcRequest.getConnectorName(), "DEFAULT");
    }

	@Override
	public Map<String, Object> execute(IDaoRequest daoRequest)
    {
		Map<String, Object> resultMap = new HashMap<>();

		RfcRequest rfcRequest = null;

		JCoDestination jCoDestination = null;
		JCoFunction jCoFunction = null;

		StopWatch stopWatch = new StopWatch();

        try
        {
        	rfcRequest = (RfcRequest)daoRequest;

        	logger.debug("[" + getLandscape(rfcRequest) + "] " + rfcRequest.getFunctionName());

        	jCoDestination = jCoConnectionResource.getDestination(getLandscape(rfcRequest));
            jCoFunction = getJCoFunction(jCoDestination, rfcRequest.getFunctionName());

            setParameter(jCoFunction, rfcRequest.getParameterSource().getValues());

            stopWatch.start();

            //Write the request log in console.
            int rfcSeq = MyLocalLogger.rfcRequestLog(rfcRequest);

            jCoFunction.execute(jCoDestination);

            stopWatch.stop();

            //Write the result log in console.
            MyLocalLogger.rfcResultLog(rfcSeq, stopWatch, jCoFunction);

            resultMap.put("RESULT", extractData(jCoFunction, rfcRequest.getOutputParameterMap()));
        }
        catch (Exception e)
        {
        	if (!stopWatch.isStopped())
        	{
        		stopWatch.stop();
        	}

        	resultMap.put("ERROR_MSG", ExceptionUtils.getStackTrace(e));

        	e.printStackTrace();
        }
        finally
        {
        	resultMap.put("START_TIME", stopWatch.getStartTime());
        	resultMap.put("ELAPSED", stopWatch.getNanoTime());

        	resultMap.put("DESTINATION", CmStringUtils.substringBetween(jCoDestination.toString(), "{", "}"));
        }

        return resultMap;
    }

    private JCoFunction getJCoFunction(JCoDestination jCoDestination, String functionName) throws JCoException
    {
        return jCoDestination.getRepository().getFunction(functionName);
    }

    private void setParameter(JCoFunction jCoFunction, Map parameterMap) throws JCoException
    {
        if (!CollectionUtils.isEmpty(parameterMap))
        {
            JCoParameterList[] jCoParameterList = {jCoFunction.getImportParameterList(), jCoFunction.getExportParameterList(), jCoFunction.getChangingParameterList(), jCoFunction.getTableParameterList()};

            String name = null;
            Object value = null;
            int type = 0;

            Iterator iterator = null;
            Iterator subIterator = null;
            Entry entry = null;
            Entry subEntry = null;
            JCoStructure jCoStructure = null;
            JCoTable jCoTable = null;
            Map tableMap = null;
            Map structureMap = null;
            MapParameterSource mapParameterSource = null;
            ParameterValue parameterValue = null;
            ParameterValue subParameterValue = null;
            Map valueMap = null;
            String subName = null;
            Object subValue = null;

            for (int i=0, length=jCoParameterList.length; i<length; i++)
            {
                if (jCoParameterList[i] != null)
                {
                    JCoMetaData jCoMetaData = jCoParameterList[i].getMetaData();

                    if (jCoMetaData != null)
                    {
                        for (int j=0, size=jCoMetaData.getFieldCount(); j<size; j++)
                        {
                            name = jCoMetaData.getName(j);

                            if (name == null || "".equals(name))
                            {
                                continue;
                            }

                            name = name.toUpperCase();
                            parameterValue = (ParameterValue)parameterMap.get(name);

                            if (parameterValue == null)
                            {
                                continue;
                            }

                            value = parameterValue.getValue();

                            if (value == null)
                            {
                                continue;
                            }

                            type = jCoMetaData.getType(j);

                            if (type == AbapType.STRUCTURE)//structure
                            {
                                mapParameterSource = (MapParameterSource)value;

                                structureMap = mapParameterSource.getValues();

                                if (CollectionUtils.isEmpty(structureMap))
                                {
                                    continue;
                                }

                                jCoStructure = jCoParameterList[i].getStructure(name);

                                iterator = structureMap.entrySet().iterator();

                                while (iterator.hasNext())
                                {
                                    entry = (Entry)iterator.next();

                                    subParameterValue = (ParameterValue)entry.getValue();

                                    if (subParameterValue == null)
                                    {
                                        continue;
                                    }

                                    subValue = subParameterValue.getValue();

                                    if (subValue == null)
                                    {
                                        continue;
                                    }

                                    jCoStructure.setValue((String)entry.getKey(), subValue);
                                }
                            }
                            else if (type == AbapType.TABLE)//table
                            {
                                mapParameterSource = (MapParameterSource)value;

                                tableMap = mapParameterSource.getValues();

                                if (CollectionUtils.isEmpty(tableMap))
                                {
                                    continue;
                                }

                                jCoTable = jCoParameterList[i].getTable(name);

                                iterator = tableMap.entrySet().iterator();

                                while (iterator.hasNext())
                                {
                                    entry = (Entry)iterator.next();

                                    valueMap = (Map)((ParameterValue)entry.getValue()).getValue();

                                    if (!CollectionUtils.isEmpty(valueMap))
                                    {
                                        jCoTable.appendRow();

                                        subIterator = valueMap.entrySet().iterator();

                                        while (subIterator.hasNext())
                                        {
                                            subEntry = (Entry)subIterator.next();

                                            subName = (String)subEntry.getKey();

                                            if (subName == null || "".equals(subName))
                                            {
                                                continue;
                                            }

                                            subValue = subEntry.getValue();

                                            if (subValue != null)
                                            {
                                                jCoTable.setValue(subName, subValue);
                                            }
                                        }
                                    }
                                }
                            }
                            else
                            {
                            	jCoParameterList[i].setValue(name, value);
                            }
                        }
                    }
                }
            }
        }
    }

    private Map extractData(JCoFunction jCoFunction, Map outputParameterMap)
    {
        Map resultMap = new LinkedHashMap();

        JCoParameterList[] jCoParameterList = {jCoFunction.getImportParameterList(), jCoFunction.getExportParameterList(), jCoFunction.getChangingParameterList(), jCoFunction.getTableParameterList()};

        String name = null;
        Object value = null;
        int type = 0;

        JCoTable jCoTable = null;
        JCoStructure jCoStructure = null;

        Map structureMap = null;
        Map fieldMap = null;
        List tableList = null;
        RowMapper rowMapper = null;

        for (int i=0, length=jCoParameterList.length; i<length; i++)
        {
            if (jCoParameterList[i] != null)
            {
                JCoMetaData jCoMetaData = jCoParameterList[i].getMetaData();

                if (jCoMetaData != null)
                {
                    for (int j=0, size=jCoMetaData.getFieldCount(); j<size; j++)
                    {
                        name = jCoMetaData.getName(j).toUpperCase();

                        if (!isOutputTarget(outputParameterMap, name))
                        {
                            continue;
                        }

                        type = jCoMetaData.getType(j);

                        if (type == AbapType.STRUCTURE)//structure
                        {
                            jCoStructure = jCoParameterList[i].getStructure(name);

                            if (jCoStructure == null || jCoStructure.getFieldCount() == 0)
                            {
                                continue;
                            }

                            structureMap = getRowMapper(outputParameterMap, name, DEFAULT_ROW_MAPPER).mapRow(jCoStructure.getRecordFieldIterator());

                            if (!structureMap.isEmpty())
                            {
                                resultMap.put(name, structureMap);
                            }
                        }
                        else if (type == AbapType.TABLE)//table
                        {
                            jCoTable = jCoParameterList[i].getTable(name);

                            if (jCoTable == null || jCoTable.getNumRows() == 0)
                            {
                                continue;
                            }

                            tableList = new ArrayList();

                            rowMapper = getRowMapper(outputParameterMap, name, DEFAULT_ROW_MAPPER);

                            do
                            {
                                fieldMap = rowMapper.mapRow(jCoTable.getRecordFieldIterator());

                                if (!fieldMap.isEmpty())
                                {
                                    tableList.add(fieldMap);
                                }
                            }
                            while (jCoTable.nextRow());

                            if (!tableList.isEmpty())
                            {
                                resultMap.put(name, tableList);
                            }
                        }
                        else
                        {
                            value = jCoParameterList[i].getValue(name);

                            if (value != null)
                            {
                                resultMap.put(name, value);
                            }
                        }
                    }
                }
            }
        }

        logger.debug(resultMap.toString());

        return resultMap;
    }
}
