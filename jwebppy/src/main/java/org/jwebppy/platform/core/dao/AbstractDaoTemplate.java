package org.jwebppy.platform.core.dao;

import java.util.Map;

import org.jwebppy.platform.core.dao.sap.RowMapper;
import org.springframework.util.CollectionUtils;

public abstract class AbstractDaoTemplate implements IDaoTemplate
{
    protected RowMapper getRowMapper(Map outputParameterMap, String name, RowMapper defaultRowMapper)
    {
        if (outputParameterMap != null)
        {
            OutputParameter outputParameter = (OutputParameter)outputParameterMap.get(name);

            if (outputParameter != null)
            {
                RowMapper rowMapper = outputParameter.getRowMapper();

                if (rowMapper != null)
                {
                    return outputParameter.getRowMapper();
                }
            }
        }

        return defaultRowMapper;
    }

    protected boolean isOutputTarget(Map outputParameterMap, String key)
    {
    	if (!CollectionUtils.isEmpty(outputParameterMap))
    	{
    		if (outputParameterMap.containsKey(key))
    		{
    			return true;
    		}

    		return false;
    	}

        return true;
    }

	@Override
	public abstract Map<String, Object> execute(IDaoRequest daoRequest);
}
