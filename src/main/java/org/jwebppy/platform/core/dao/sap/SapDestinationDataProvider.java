package org.jwebppy.platform.core.dao.sap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

public class SapDestinationDataProvider implements DestinationDataProvider
{
    private DestinationDataEventListener destinationDataEventListener;
    private final Map<String, Properties> propertiesMap = Collections.synchronizedMap(new HashMap<>());

    public void setDestinationProperties(String destination, Properties properties)
    {
        Properties currentProperties = propertiesMap.get(destination);

        if (currentProperties == null && properties != null)
        {
            propertiesMap.put(destination, properties);
        }
        else
        {
            if (properties == null)
            {
                destinationDataEventListener.deleted(destination);
                propertiesMap.remove(destination);
            }
            else
            {
                destinationDataEventListener.updated(destination);

                propertiesMap.remove(destination);
                propertiesMap.put(destination, properties);
            }
        }
    }

    @Override
	public Properties getDestinationProperties(String destination)
    {
        return propertiesMap.get(destination);
    }

    @Override
	public boolean supportsEvents()
    {
        return true;
    }

    @Override
	public void setDestinationDataEventListener(DestinationDataEventListener destinationDataEventListener)
    {
        this.destinationDataEventListener = destinationDataEventListener;
    }
}
