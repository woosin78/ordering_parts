package org.jwebppy.portal.scm.completes;

import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CompletesGeneralService
{
	@Autowired(required = false)
	private SimpleRfcTemplate simpleRfcTemplate;

	@Cacheable(value = CacheConfig.CUSTOMER, key = "#username", unless="#result == null")
	public RfcResponse getErpUserInfo(String username)
	{
		if (CmStringUtils.isNotEmpty(username))
		{
			RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_USER_PARAMETERS");

			rfcRequest.addField("I_USERID", CmStringUtils.upperCase(username));
			rfcRequest.addField("I_BGTYP", "S");

			return simpleRfcTemplate.response(rfcRequest);
		}

		return null;
	}
}
