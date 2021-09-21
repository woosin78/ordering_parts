package org.jwebppy.platform.core.dao.sap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.jwebppy.config.JCoConfig;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;

public class JCoConnectionResource
{
	private Logger logger = LoggerFactory.getLogger(JCoConfig.class);

    @Value("${sap.jco.env}")
    private String SAP_JCO_ENV;

    private Map<String, JCoConnectionInfo> connectionInfoMap;
    private SapDestinationDataProvider sapDestinationDataProvider;
    private String defaultLandscape;

    public JCoConnectionResource() {}

    public JCoConnectionResource(Map<String, JCoConnectionInfo> connectionInfoMap)
    {
    	setConnectionInfoMap(connectionInfoMap);
    }

    public void addConnectionInfo(JCoConnectionInfo jCoConnectionInfo)
    {
    	if (jCoConnectionInfo != null)
    	{
        	if (connectionInfoMap == null)
        	{
        		connectionInfoMap = new HashMap<>();
        	}

        	if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getName()))
        	{
        		connectionInfoMap.put(jCoConnectionInfo.getName(), jCoConnectionInfo);
        	}
    	}
    }

    public void initialize()
    {
        if (connectionInfoMap != null)
        {
            sapDestinationDataProvider = new SapDestinationDataProvider();

            Iterator<Entry<String, JCoConnectionInfo>> iterator = connectionInfoMap.entrySet().iterator();
            Entry<String, JCoConnectionInfo> entry = null;
            JCoConnectionInfo jCoConnectionInfo = null;

            while (iterator.hasNext())
            {
                entry = iterator.next();

                jCoConnectionInfo = entry.getValue();

                Properties properties = new Properties();

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getAshost()))
                {
                    properties.setProperty(DestinationDataProvider.JCO_ASHOST, jCoConnectionInfo.getAshost());
                }

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getMshost()))
                {
                	properties.setProperty(DestinationDataProvider.JCO_MSHOST, jCoConnectionInfo.getMshost());
                }

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getGroup()))
                {
                	properties.setProperty(DestinationDataProvider.JCO_GROUP, jCoConnectionInfo.getGroup());
                }

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getR3name()))
                {
                	properties.setProperty(DestinationDataProvider.JCO_R3NAME, jCoConnectionInfo.getR3name());
                }

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getSysnr()))
                {
                    properties.setProperty(DestinationDataProvider.JCO_SYSNR, jCoConnectionInfo.getSysnr());
                }

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getClient()))
                {
                    properties.setProperty(DestinationDataProvider.JCO_CLIENT, jCoConnectionInfo.getClient());
                }

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getUser()))
                {
                    properties.setProperty(DestinationDataProvider.JCO_USER, jCoConnectionInfo.getUser());
                }

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getPassword()))
                {
                    properties.setProperty(DestinationDataProvider.JCO_PASSWD, jCoConnectionInfo.getPassword());
                }

                properties.setProperty(DestinationDataProvider.JCO_LANG, CmStringUtils.defaultString(jCoConnectionInfo.getLanguage(), "EN"));

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getPoolCapacity()))
                {
                    properties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, jCoConnectionInfo.getPoolCapacity());
                }

                if (CmStringUtils.isNotEmpty(jCoConnectionInfo.getPeakLimit()))
                {
                    properties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, jCoConnectionInfo.getPeakLimit());
                }

                sapDestinationDataProvider.setDestinationProperties(entry.getKey(), properties);

                logger.info("[" + SAP_JCO_ENV + "] The SAP Landscape has been loaded [" + entry.getKey() + "]");
            }

            Environment.registerDestinationDataProvider(sapDestinationDataProvider);
        }
    }

    public void setConnectionInfoMap(Map<String, JCoConnectionInfo> connectionInfoMap)
    {
        this.connectionInfoMap = connectionInfoMap;
    }

    public Map<String, JCoConnectionInfo> getConnectionInfoMap()
    {
        return Collections.unmodifiableMap(connectionInfoMap);
    }

    public JCoDestination getDestination(String destination) throws JCoException
    {
    	if (destination == null || "".equals(destination))
    	{
    		destination = defaultLandscape;
    	}

        return JCoDestinationManager.getDestination(destination.trim());
    }

    public void changeDestinationData(String destination, Properties properties)
    {
        sapDestinationDataProvider.setDestinationProperties(destination, properties);
    }

    public JCoConnectionInfo getConnectionInfo(String landscape)
    {
        return connectionInfoMap.get(landscape);
    }

    public void setDefaultLandscape(String defaultLandscape)
    {
    	this.defaultLandscape = defaultLandscape;
    }
}
