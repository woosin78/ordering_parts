package org.jwebppy.config;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.dao.sap.JCoConnectionInfo;
import org.jwebppy.platform.core.dao.sap.JCoConnectionResource;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.conn_resource.service.SapConnResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JCoConfig
{
    @Value("${sap.jco.enable}")
    private boolean SAP_JCO_ENABLE;

    @Autowired
    private SapConnResourceService sapConnResourceService;

    @Bean
    public SimpleRfcTemplate rfcTemplate()
	{
    	if (SAP_JCO_ENABLE)
		{
			return new SimpleRfcTemplate(jCoConnectionResource());
		}

		return null;
	}

    /*
     * SAP connector JCo Client Properties
     * https://docs.mulesoft.com/sap-connector/0.3.7/sap-connector-advanced-features#jco-extended-properties
     */
    private JCoConnectionResource jCoConnectionResource()
    {
    	JCoConnectionResource jCoConnectionResource = new JCoConnectionResource();

    	List<SapConnResourceDto> sapConnResources = ListUtils.emptyIfNull(sapConnResourceService.getAvailableSapConnResources());

    	for (SapConnResourceDto sapConnResource: sapConnResources)
    	{
			JCoConnectionInfo jCoConnectionInfo = JCoConnectionInfo.builder()
					.name(sapConnResource.getName())
					.ashost(sapConnResource.getAppServer())
					.mshost(sapConnResource.getMsgServer())
					.group(sapConnResource.getGrpServer())
					.client(sapConnResource.getClient())
					.r3name(sapConnResource.getR3name())
					.sysnr(sapConnResource.getInstanceNo())//Instance Number
					.user(sapConnResource.getUsername())
					.password(sapConnResource.getPassword())
					.language(sapConnResource.getLanguage())
					.poolCapacity(sapConnResource.getPoolCapacity())
					.peakLimit(sapConnResource.getPeakLimit())
					.build();

			jCoConnectionResource.addConnectionInfo(jCoConnectionInfo);
    	}

    	jCoConnectionResource.initialize();

    	return jCoConnectionResource;
    }
}
