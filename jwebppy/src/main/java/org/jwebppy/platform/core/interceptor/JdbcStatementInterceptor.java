package org.jwebppy.platform.core.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.jwebppy.platform.core.util.CmBeanUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.JdbcStatementContextUtils;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.dto.IfType;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;

@Intercepts({
	@Signature(type=StatementHandler.class, method="update", args={Statement.class})
    ,@Signature(type=StatementHandler.class, method="query", args={Statement.class, ResultHandler.class})
    ,@Signature(type=StatementHandler.class, method="batch", args={Statement.class})
})
public class JdbcStatementInterceptor implements Interceptor
{
	@Override
	public Object intercept(Invocation invocation) throws Throwable
	{
		StopWatch stopWatch = new StopWatch();
		Object result = null;

		try
		{
			stopWatch.start();
			result = invocation.proceed();
			stopWatch.stop();
		}
		catch (Exception e)
		{
			stopWatch.stop();

			e.printStackTrace();
			throw e;
		}
		finally
		{
			writeLog(invocation, stopWatch);
		}

		return result;
	}

	private void writeLog(Invocation invocation, StopWatch stopWatch)
	{
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();

		Object parameterObject = statementHandler.getParameterHandler().getParameterObject();

		if (parameterObject != null && !CmStringUtils.startsWith(parameterObject.getClass().getName(), "org.jwebppy.platform.mgmt.logging"))
		{
			BoundSql boundSql = statementHandler.getBoundSql();

			Map<String, Object> parameterMap = new LinkedHashMap<>();

			if (CmBeanUtils.isSimpleValueType(parameterObject.getClass()))
			{
				parameterMap.put(parameterObject.getClass().getName(), parameterObject);
			}
			else if (parameterObject instanceof Map)
			{
				for (ParameterMapping parameterMapping : boundSql.getParameterMappings())
				{
				    String key = parameterMapping.getProperty();

				    parameterMap.put(key, ((Map<?, ?>)parameterObject).get(key));
				}
			}
			else
			{
				List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

				for (ParameterMapping parameterMapping : parameterMappings)
				{
				    String propertyValue = parameterMapping.getProperty();

				    try
				    {
				    	if (!parameterMap.containsKey(propertyValue))
				    	{
				    		parameterMap.put(propertyValue, BeanUtils.getProperty(parameterObject, propertyValue));
				    	}
					}
				    catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e)
				    {
						//e.printStackTrace();
					}
				}
			}

			DataAccessLogDto dataAccessLog = new DataAccessLogDto();
			dataAccessLog.setDlSeq(UidGenerateUtils.generate());
			dataAccessLog.setType(IfType.J);
			dataAccessLog.setStartTime(stopWatch.getStartTime());
			dataAccessLog.setElapsed(stopWatch.getNanoTime());
			dataAccessLog.setDataAccessLogParameters(makeParameters(parameterMap));
			dataAccessLog.setCommand(getSql(statementHandler.getBoundSql()));

			JdbcStatementContextUtils.put(dataAccessLog);
		}
	}

	private List<DataAccessLogParameterDto> makeParameters(Map<String, Object> parameterMap)
	{
		List<DataAccessLogParameterDto> dataAccessLogParameters = new LinkedList<>();

		if (MapUtils.isNotEmpty(parameterMap))
		{
			Set<String> set = parameterMap.keySet();
			Iterator<String> iterator = set.iterator();
			List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails = new LinkedList<>();

			while (iterator.hasNext())
			{
				String key = CmStringUtils.trimToEmpty(iterator.next());

				DataAccessLogParameterDetailDto dataAccessLogParameterDetail = new DataAccessLogParameterDetailDto();
				dataAccessLogParameterDetail.setLineNo(0);
				dataAccessLogParameterDetail.setName(key);
				dataAccessLogParameterDetail.setValue(CmStringUtils.trimToEmpty(parameterMap.get(key)));

				dataAccessLogParameterDetails.add(dataAccessLogParameterDetail);
			}

			DataAccessLogParameterDto dataAccessLogParameter = new DataAccessLogParameterDto();
			dataAccessLogParameter.setType(ParameterType.S);
			dataAccessLogParameter.setDataAccessLogParameterDetails(dataAccessLogParameterDetails);

			dataAccessLogParameters.add(dataAccessLogParameter);

			return dataAccessLogParameters;
		}

		return null;
	}

	private String getSql(BoundSql boundSql)
	{
		String sql = CmStringUtils.trim(boundSql.getSql());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

		if (CollectionUtils.isNotEmpty(parameterMappings))
		{
			for (ParameterMapping parameterMapping : parameterMappings)
			{
				sql = CmStringUtils.replace(sql, "?", "@" + parameterMapping.getProperty(), 1);
			}
		}

		return sql;
	}
}
