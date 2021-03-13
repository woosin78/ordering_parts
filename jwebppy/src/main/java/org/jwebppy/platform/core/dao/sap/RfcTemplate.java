package org.jwebppy.platform.core.dao.sap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.dao.AbstractDaoTemplate;
import org.jwebppy.platform.core.dao.IDaoRequest;
import org.jwebppy.platform.core.dao.MapParameterSource;
import org.jwebppy.platform.core.dao.ParameterValue;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.SessionContextUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private DataAccessLogService dataAccessLogService;

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

    private String getLandscape(RfcRequest rfcRequest)
    {
    	if (CmStringUtils.isNotEmpty(rfcRequest.getLandscape()))
    	{
    		return rfcRequest.getLandscape();
    	}

    	ErpUserContext erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();

    	if (erpUserContext != null)
    	{
        	if (CmStringUtils.equals(erpUserContext.getCorpName(), "DIVEU"))
        	{
        		return "P01";
        	}
        	else if (CmStringUtils.equals(erpUserContext.getCorpName(), "DIVUK"))
        	{
        		return "GMT";
        	}
    	}

    	return "P09";
    }

	@Override
	public Map<String, Object> execute(IDaoRequest daoRequest)
    {
		RfcRequest rfcRequest = null;
		long startTime = 0;
		long elapsedTime = 0;
		String errorMsg = null;

		JCoDestination jCoDestination = null;
		JCoFunction jCoFunction = null;

        try
        {
        	rfcRequest = (RfcRequest)daoRequest;

        	logger.debug("[" + getLandscape(rfcRequest) + "] " + rfcRequest.getFunctionName());

        	jCoDestination = jCoConnectionResource.getDestination(getLandscape(rfcRequest));

            jCoFunction = getJCoFunction(jCoDestination, rfcRequest.getFunctionName());

            setParameter(jCoFunction, rfcRequest.getParameterSource().getValues());

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            jCoFunction.execute(jCoDestination);

            stopWatch.stop();

            startTime = stopWatch.getStartTime();
            elapsedTime = stopWatch.getNanoTime();

            return extractData(jCoFunction, rfcRequest.getOutputParameterMap());
        }
        catch (JCoException e)
        {
        	errorMsg = ExceptionUtils.getStackTrace(e);

        	throw new RfcDataAccessException(e.getGroup(), e.getMessage(), e.getCause());
        }
        finally
        {
			StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
			String className = null;
			String methodName = null;

			for (int i=0, length=stackTraceElements.length; i<length; i++)
			{
				className = stackTraceElements[i].getClassName();

				if (className.startsWith("org.jwebppy.platform.core.dao.sap") || className.startsWith("org.springframework"))
				{
					continue;
				}

				methodName = stackTraceElements[i].getMethodName();

				break;
			}

			DataAccessLogDto dataAccessLog = new DataAccessLogDto();
			dataAccessLog.setCommand(rfcRequest.getFunctionName());
			dataAccessLog.setType("R");
			dataAccessLog.setClassName(className);
			dataAccessLog.setMethodName(methodName);
			dataAccessLog.setRequestId(MDC.get(PlatformConfigVo.REQUEST_MDC_UUID_TOKEN_KEY));
			dataAccessLog.setSessionId(SessionContextUtils.getSessionId());
			dataAccessLog.setError(errorMsg);
			dataAccessLog.setDestination(jCoDestination.getDestinationID());
			dataAccessLog.setStartTime(startTime);
			dataAccessLog.setElapsed(elapsedTime);
			dataAccessLog.setDataAccessLogParameters(makeParameters(rfcRequest));
			dataAccessLog.setRegUsername(UserAuthenticationUtils.getUsername());

			dataAccessLogService.writeLog(dataAccessLog);

			jCoFunction = null;
			jCoDestination = null;
        }
    }

    private JCoFunction getJCoFunction(JCoDestination jCoDestination, String functionName) throws JCoException
    {
        return jCoDestination.getRepository().getFunction(functionName);
    }

    private void setParameter(JCoFunction jCoFunction, Map parameterMap)
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

                        if (type == AbapType.FIELD)//scalar
                        {
                            value = jCoParameterList[i].getValue(name);

                            if (value != null)
                            {
                                resultMap.put(name, value);
                            }
                        }
                        else if (type == AbapType.STRUCTURE)//structure
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
                    }
                }
            }
        }

        logger.debug(resultMap.toString());

        return resultMap;
    }

	private List<DataAccessLogParameterDto> makeParameters(RfcRequest rfcRequest)
	{
		Map<String, ParameterValue> dataMap = rfcRequest.getParameterSource().getValues();

		if (MapUtils.isNotEmpty(dataMap))
		{
			List<DataAccessLogParameterDto> dataAccessLogParameters = new LinkedList<>();

			Iterator<Entry<String, ParameterValue>> iterator = dataMap.entrySet().iterator();

			ParameterValue parameterValue;
			Entry<String, ParameterValue> entry;
			int type = 0;

			while (iterator.hasNext())
			{
				entry = iterator.next();

				parameterValue = entry.getValue();
				type = parameterValue.getType();

				DataAccessLogParameterDto dataAccessLogParameter = new DataAccessLogParameterDto();
				dataAccessLogParameter.setName(entry.getKey());

				if (type == AbapType.STRUCTURE)
				{
					MapParameterSource mapParameterSource = (MapParameterSource)parameterValue.getValue();

					dataAccessLogParameter.setType("S");
					dataAccessLogParameter.setDataAccessLogParameterDetails(structureToParameterDetails(0, mapParameterSource.getValues()));
				}
				else if (type == AbapType.TABLE)
				{
					MapParameterSource mapParameterSource = (MapParameterSource)parameterValue.getValue();

					dataAccessLogParameter.setType("T");
					dataAccessLogParameter.setDataAccessLogParameterDetails(tableToParameterDetails(mapParameterSource.getValues()));
				}
				else
				{
					dataAccessLogParameter.setType("F");
					dataAccessLogParameter.setDataAccessLogParameterDetails(fieldToParameterDetails(parameterValue));
				}

				dataAccessLogParameters.add(dataAccessLogParameter);
			}

			return dataAccessLogParameters;
		}

		return null;
	}

	private List<DataAccessLogParameterDetailDto> fieldToParameterDetails(ParameterValue parameterValue)
	{
		List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails = null;

		if (parameterValue.getValue() != null)
		{
			dataAccessLogParameterDetails = new ArrayList<>();

			DataAccessLogParameterDetailDto dataAccessLogParameterDetail = new DataAccessLogParameterDetailDto();
			dataAccessLogParameterDetail.setLineNo(0);
			dataAccessLogParameterDetail.setName(parameterValue.getName());
			dataAccessLogParameterDetail.setValue(CmStringUtils.trimToEmpty(parameterValue.getValue()));

			dataAccessLogParameterDetails.add(dataAccessLogParameterDetail);
		}

		return dataAccessLogParameterDetails;
	}

	private List<DataAccessLogParameterDetailDto> structureToParameterDetails(int lineNo, Map<String, ParameterValue> valueMap)
	{
		if (MapUtils.isNotEmpty(valueMap))
		{
			List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails = new LinkedList<>();

			Iterator<Entry<String, ParameterValue>> iterator = valueMap.entrySet().iterator();

			while (iterator.hasNext())
			{
				Entry<String, ParameterValue> entry = iterator.next();

				ParameterValue parameterValue = entry.getValue();

				if (parameterValue.getValue() == null)
				{
					continue;
				}

				DataAccessLogParameterDetailDto dataAccessLogParameterDetail = new DataAccessLogParameterDetailDto();
				dataAccessLogParameterDetail.setLineNo(lineNo);
				dataAccessLogParameterDetail.setName(entry.getKey());
				dataAccessLogParameterDetail.setValue(CmStringUtils.trimToEmpty(parameterValue.getValue()));

				dataAccessLogParameterDetails.add(dataAccessLogParameterDetail);
			}

			return dataAccessLogParameterDetails;
		}

		return null;
	}

	private List<DataAccessLogParameterDetailDto> tableToParameterDetails(Map<String, ParameterValue> valueMap)
	{
		if (MapUtils.isNotEmpty(valueMap))
		{
			List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails = new LinkedList<>();

			Iterator<Entry<String, ParameterValue>> iterator = valueMap.entrySet().iterator();
			int lineNo = 0;

			while (iterator.hasNext())
			{
				Entry<String, ParameterValue> entry = iterator.next();

				Map<String, Object> subValueMap = (Map<String, Object>)entry.getValue().getValue();

				if (MapUtils.isEmpty(subValueMap))
				{
					continue;
				}

				Iterator<Entry<String, Object>> subIterator = subValueMap.entrySet().iterator();

				while (subIterator.hasNext())
				{
					Entry<String, Object> subEntry = subIterator.next();

					if (CmStringUtils.isNotEmpty(subEntry.getValue()))
					{
						DataAccessLogParameterDetailDto dataAccessLogParameterDetail = new DataAccessLogParameterDetailDto();
						dataAccessLogParameterDetail.setLineNo(lineNo);
						dataAccessLogParameterDetail.setName(subEntry.getKey());
						dataAccessLogParameterDetail.setValue(subEntry.getValue().toString());

						dataAccessLogParameterDetails.add(dataAccessLogParameterDetail);
					}
				}

				lineNo++;
			}

			return dataAccessLogParameterDetails;
		}

		return null;
	}
}
