package org.jwebppy.portal.common.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class PortalGeneralService
{
	@Autowired
	protected SimpleRfcTemplate simpleRfcTemplate;

	protected RfcResponse getErpUserInfo(String username, String bg, String module)
	{
		if (CmStringUtils.isNotEmpty(username))
		{
			RfcRequest rfcRequest = new RfcRequest("ZSS_PARA_DIV_EP_GET_USER_PARAM");

			rfcRequest.
				field(new Object[][] {
					{"I_USERID", CmStringUtils.upperCase(username)},
					{"I_BGCON", bg},
					{"I_BGTYP", module}
				});

			return simpleRfcTemplate.response(rfcRequest);
		}

		return null;
	}

	protected void setErpUserInfo(String bg, String module)
	{
		if (UserAuthenticationUtils.getUserDetails().getErpUserContext() == null)
		{
	        RfcResponse response = getErpUserInfo(UserAuthenticationUtils.getUsername(), bg, module);
	        DataList userList = response.getTable("T_USER");

	        if (CollectionUtils.isNotEmpty(userList))
	        {
	    		DataMap userMap = (DataMap)userList.get(0);

	        	ErpUserContext erpUserContext = ErpUserContext.builder()
	        			.corp(userMap.getString("BUKRS"))
	        			.username(userMap.getString("BNAME").toUpperCase())
	        			.custCode(userMap.getString("KUNNR"))
	        			.custName(userMap.getString("NAME1"))
	        			.salesOrg(userMap.getString("VKORG"))
	        			.distChl(userMap.getString("VTWEG"))
	        			.division(userMap.getString("SPART"))
	        			.custGrp1(userMap.getString("KVGR1"))
	        			.custGrp2(userMap.getString("KVGR2"))
	        			.custGrp3(userMap.getString("KVGR3"))
	        			.custGrp4(userMap.getString("KVGR4"))
	        			.custGrp5(userMap.getString("KVGR5"))
	        			.shippingCondition(userMap.getString("VSBED"))
	        			.build();

	    		UserAuthenticationUtils.getUserDetails().setErpUserContext(erpUserContext);
	        }
		}
	}

	protected ErpDataMap getErpUserInfo(String bg, String module)
	{
		ErpUserContext erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();

		if (ObjectUtils.isEmpty(erpUserContext))
		{
			setErpUserInfo(bg, module);

			erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();
		}

		ErpDataMap erpDataMap = new ErpDataMap();

		erpDataMap.add(new Object[][] {
			{"BUKRS", erpUserContext.getCorp()},
			{"BNAME", erpUserContext.getUsername()},
			{"KUNNR", erpUserContext.getCustCode()},
			{"NAME1", erpUserContext.getCustName()},
			{"VKORG", erpUserContext.getSalesOrg()},
			{"VTWEG", erpUserContext.getDistChl()},
			{"SPART", erpUserContext.getDivision()},
			{"KVGR1", erpUserContext.getCustGrp1()},
			{"KVGR2", erpUserContext.getCustGrp2()},
			{"KVGR3", erpUserContext.getCustGrp3()},
			{"KVGR4", erpUserContext.getCustGrp4()},
			{"KVGR5", erpUserContext.getCustGrp5()},
			{"LANG", UserAuthenticationUtils.getLanguage()}
		});

		return erpDataMap;
	}
}
