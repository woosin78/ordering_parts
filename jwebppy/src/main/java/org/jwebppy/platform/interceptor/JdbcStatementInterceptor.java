package org.jwebppy.platform.interceptor;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.jwebppy.platform.core.util.CmFieldUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.JdbcStatementContextUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;

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

		BoundSql boundSql = statementHandler.getBoundSql();
		Object parameterObject = statementHandler.getParameterHandler().getParameterObject();

		DataAccessLogDto dataAccessLog = new DataAccessLogDto();
		dataAccessLog.setCommand(CmStringUtils.trim(boundSql.getSql()));
		dataAccessLog.setType("J");
		dataAccessLog.setStartTime(stopWatch.getStartTime());
		dataAccessLog.setElapsed(stopWatch.getNanoTime());

		if (parameterObject != null && !CmStringUtils.startsWith(parameterObject.getClass().getName(), "org.jwebppy.platform.mgmt.logging"))
		{
			Map<String, Object> parameterMap = new LinkedHashMap<>();

			if (CmBeanUtils.isSimpleValueType(parameterObject.getClass()))
			{
				parameterMap.put(parameterObject.getClass().getName(), parameterObject);
			}
			else if (parameterObject instanceof Map)
			{
				List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

				for (ParameterMapping parameterMapping : parameterMappings)
				{
				    String key = parameterMapping.getProperty();

				    parameterMap.put(key, ((Map<?, ?>)parameterObject).get(key));
				}
			}
			else
			{
				List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

				Class<? extends Object> clazz = parameterObject.getClass();
				Field[] fields = CmFieldUtils.getAllFields(clazz);

				for (ParameterMapping parameterMapping : parameterMappings)
				{
				    String propertyValue = parameterMapping.getProperty();

				    try
				    {
					    Field field = clazz.getDeclaredField(propertyValue);
					    field.setAccessible(true);

					    parameterMap.put(field.getName(), field.get(parameterObject));
				    }
				    catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
				    {
				    	//<foreach>인 경우 propKey가 "__frch_%아이템명%_반복횟수"
				    	if (boundSql.hasAdditionalParameter(propertyValue))
				    	{
				    		parameterMap.put(propertyValue, boundSql.getAdditionalParameter(propertyValue));
				    	}
				    	else
				    	{
				    		//Super class 에서 정의된 필드 검색
					    	for (Field field : fields)
					    	{
					    		field.setAccessible(true);

					    		if (propertyValue.equals(field.getName()))
					    		{
					    			try
					    			{
										parameterMap.put(field.getName(), field.get(parameterObject));
										break;
									}
					    			catch (IllegalArgumentException | IllegalAccessException e1)
					    			{
										e1.printStackTrace();
									}
					    		}
					    	}
				    	}
				    }
				}
			}

			dataAccessLog.setDataAccessLogParameters(makeParameters(parameterMap));
		}

		JdbcStatementContextUtils.put(dataAccessLog);
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
			dataAccessLogParameter.setType("S");
			dataAccessLogParameter.setDataAccessLogParameterDetails(dataAccessLogParameterDetails);

			dataAccessLogParameters.add(dataAccessLogParameter);

			return dataAccessLogParameters;
		}

		return null;
	}
}
