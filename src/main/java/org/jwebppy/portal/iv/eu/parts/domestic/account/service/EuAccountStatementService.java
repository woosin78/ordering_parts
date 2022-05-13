package org.jwebppy.portal.iv.eu.parts.domestic.account.service;

import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EuAccountStatementService
{
	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	public RfcResponse getExcelDownload(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_STATE_ACC_REP");
		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_BUDAT", paramMap.getString("pDate"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());

		return simpleRfcTemplate.response(rfcRequest);
	}

	public DataList getPdfDownload(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_STATEMENT_ACCOUNT_REPORT");

		rfcRequest.addField("I_BGTYP", "P");
		rfcRequest.addField("I_BUDAT", paramMap.getString("pDate"));
		rfcRequest.addField("I_USERID", paramMap.getUsername());
		rfcRequest.addField("I_SIMUL", "");

		rfcRequest.addOutputParameter("T_OUTTAB2");

		return simpleRfcTemplate.response(rfcRequest).getTable("T_OUTTAB2");
	}
}
