package org.jwebppy.config;

import java.io.IOException;
import java.util.Properties;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dao.sap.JCoConnectionInfo;
import org.jwebppy.platform.core.dao.sap.JCoConnectionResource;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@Configuration
public class JCoConfig
{
	private Logger logger = LoggerFactory.getLogger(JCoConfig.class);

    @Value("${sap.jco.enable}")
    private boolean SAP_JCO_ENABLE;

    @Value("${sap.jco.sid}")
    private String SAP_JCO_SID;

    @Value("${sap.jco.landscape}")
    private String SAP_JCO_LANDSCAPE;

    @Value("${sap.jco.landscape.default}")
    private String SAP_JCO_LANDSCAPE_DEFAULT;

    @Bean
    public SimpleRfcTemplate rfcTemplate()
	{
    	if (SAP_JCO_ENABLE)
		{
			return new SimpleRfcTemplate(jCoConnectionResource());
		}

		return null;
	}

	private JCoConnectionResource jCoConnectionResource()
	{
		JCoConnectionResource jCoConnectionResource = new JCoConnectionResource();

		String[] landscapes = CmStringUtils.split(SAP_JCO_LANDSCAPE, PlatformCommonVo.DELIMITER);
		for (String landscape : landscapes)
		{
			jCoConnectionResource.addConnectionInfo(getConnectionInfo(landscape));
		}

		jCoConnectionResource.initialize();

		jCoConnectionResource.setDefaultLandscape(SAP_JCO_LANDSCAPE_DEFAULT);

		return jCoConnectionResource;
	}

	private JCoConnectionInfo getConnectionInfo(String landscape)
	{
		Resource resource = new ClassPathResource("/config/sap/" + SAP_JCO_SID + "/" + landscape + ".properties");

		if (resource.exists())
		{
			Properties properties;

			try
			{
				properties = PropertiesLoaderUtils.loadProperties(resource);

				JCoConnectionInfo jCoConnectionInfo = JCoConnectionInfo.builder()
						.name(landscape)
						.ashost(properties.getProperty("JCO_ASHOST"))
						.mshost(properties.getProperty("JCO_MSHOST"))
						.group(properties.getProperty("JCO_GROUP"))
						.r3name(properties.getProperty("JCO_R3NAME"))
						.client(properties.getProperty("JCO_CLIENT"))
						.sysnr(properties.getProperty("JCO_SYSNR"))
						.landscape(properties.getProperty("JCO_LANDSCAPE"))
						.user(properties.getProperty("JCO_USER"))
						.password(properties.getProperty("JCO_PASSWORD"))
						.language(properties.getProperty("JCO_LANG"))
						.poolCapacity(properties.getProperty("JCO_POOL_CAPACITY"))
						.peakLimit(properties.getProperty("JCO_PEAK_LIMIT"))
						.build();

				logger.info("[" + SAP_JCO_SID + "] The SAP Landscape has been loaded [" + landscape + "]");

				return jCoConnectionInfo;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			logger.warn("[" + SAP_JCO_SID + "] The SAP Landscape does not exist [" + landscape + "]");
		}

		return null;
	}
}
