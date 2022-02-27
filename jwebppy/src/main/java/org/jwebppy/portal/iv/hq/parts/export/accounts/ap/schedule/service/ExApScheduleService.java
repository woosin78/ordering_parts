package org.jwebppy.portal.iv.hq.parts.export.accounts.ap.schedule.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.portal.iv.hq.parts.export.common.service.PartsExportGeneralService;
import org.springframework.stereotype.Service;

@Service
public class ExApScheduleService extends PartsExportGeneralService
{
	public RfcResponse getList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_SUSPENSE_LIST");

		rfcRequest.field("I_USERID", paramMap.getUsername());

		return simpleRfcTemplate.response(rfcRequest);
	}
}
