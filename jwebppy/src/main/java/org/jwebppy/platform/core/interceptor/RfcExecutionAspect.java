package org.jwebppy.platform.core.interceptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.dao.MapParameterSource;
import org.jwebppy.platform.core.dao.ParameterValue;
import org.jwebppy.platform.core.dao.sap.AbapType;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.SessionContextUtils;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.dto.IfType;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.jwebppy.platform.mgmt.logging.service.DataAccessResultLogService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RfcExecutionAspect
{
	@Autowired
	private DataAccessLogService dataAccessLogService;

	@Autowired
	private DataAccessResultLogService dataAccessResultLogService;

    @Around("execution(* org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate.response(..))")
    public Object onAround(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable
    {
		RfcResponse result = null;

		try
		{
			result = (RfcResponse)proceedingJoinPoint.proceed();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
	    	result.setDlSeq(writeLog(proceedingJoinPoint, result));
		}

		return result;
    }

    private String[] getLastStackTrace()
    {
    	String[] stackTrace = new String[2];

		StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();

		for (int i=0, length=stackTraceElements.length; i<length; i++)
		{
			String className = stackTraceElements[i].getClassName();
			String methodName = stackTraceElements[i].getMethodName();

			if (!CmStringUtils.startsWith(className, "org.jwebppy.") || CmStringUtils.startsWith(className, "org.jwebppy.platform.core.") || CmStringUtils.contains(className, "$$"))
			{
				continue;
			}

			stackTrace[0] = className;
			stackTrace[1] = methodName;

			break;
		}

		return stackTrace;
    }

    private String writeLog(ProceedingJoinPoint proceedingJoinPoint, RfcResponse rfcResponse)
    {
    	Object[] args = proceedingJoinPoint.getArgs();
    	RfcRequest rfcRequest = (RfcRequest)args[0];

    	String[] stackTrace = getLastStackTrace();

		String dlSeq = UidGenerateUtils.generate();

		DataAccessLogDto dataAccessLog = new DataAccessLogDto();
		dataAccessLog.setDlSeq(dlSeq);
		dataAccessLog.setCommand(rfcRequest.getFunctionName());
		dataAccessLog.setType(IfType.R);
		dataAccessLog.setClassName(stackTrace[0]);
		dataAccessLog.setMethodName(stackTrace[1]);
		dataAccessLog.setRequestId(MDC.get(PlatformConfigVo.REQUEST_MDC_UUID_TOKEN_KEY));
		dataAccessLog.setSessionId(SessionContextUtils.getSessionId());
		dataAccessLog.setDataAccessLogParameters(makeParameters(rfcRequest));

		if (UserAuthenticationUtils.isLogin())
		{
			dataAccessLog.setTimezone(UserAuthenticationUtils.getUserDetails().getTimezone());
			dataAccessLog.setRegUsername(UserAuthenticationUtils.getUsername());
		}

		if (rfcResponse != null)
		{
			dataAccessLog.setDestination(rfcResponse.getDestination());
			dataAccessLog.setStartTime(rfcResponse.getStartTime());
			dataAccessLog.setElapsed(rfcResponse.getElapsed());
			dataAccessLog.setError(rfcResponse.getErrorMsg());
		}

		dataAccessLogService.writeLog(dataAccessLog);

		dataAccessResultLogService.writeLog(dlSeq, rfcResponse);

		return dlSeq;
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

					dataAccessLogParameter.setType(ParameterType.S);
					dataAccessLogParameter.setDataAccessLogParameterDetails(structureToParameterDetails(0, mapParameterSource.getValues()));
				}
				else if (type == AbapType.TABLE)
				{
					MapParameterSource mapParameterSource = (MapParameterSource)parameterValue.getValue();

					dataAccessLogParameter.setType(ParameterType.T);
					dataAccessLogParameter.setDataAccessLogParameterDetails(tableToParameterDetails(mapParameterSource.getValues()));
				}
				else
				{
					dataAccessLogParameter.setType(ParameterType.F);
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
