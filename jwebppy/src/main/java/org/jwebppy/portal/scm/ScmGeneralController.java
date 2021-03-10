package org.jwebppy.portal.scm;

import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.security.authentication.dto.ErpUserContext;
import org.jwebppy.portal.PortalGeneralController;
import org.springframework.beans.factory.annotation.Autowired;

public class ScmGeneralController extends PortalGeneralController
{
	@Autowired
	private ScmGeneralService scmGeneralService;

	public ErpDataMap getErpUserInfo()
	{
		ErpUserContext erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();
		String lang = CmStringUtils.defaultString(UserAuthenticationUtils.getUserDetails().getLanguage(), Locale.ENGLISH).toUpperCase();

		if (erpUserContext == null || CmStringUtils.isEmpty(erpUserContext.getCorpName()))
		{
	        RfcResponse response = scmGeneralService.getUserInfo(getUsername());
	        DataList userList = response.getTable("T_USER");

	        if (CollectionUtils.isNotEmpty(userList))
	        {
	    		erpUserContext = new ErpUserContext();

	    		DataMap userMap = (DataMap)userList.get(0);

	    		userMap.put("TYPE", response.getString("O_TYPE"));

	    		erpUserContext.setCorpNo(userMap.getString("BUKRS"));
	    		erpUserContext.setUsername(userMap.getString("BNAME").toUpperCase());
	    		erpUserContext.setCustomerNo(userMap.getString("KUNNR"));
	    		erpUserContext.setCustomerName(userMap.getString("NAME1"));
	    		erpUserContext.setSalesOrg(userMap.getString("VKORG"));
	    		erpUserContext.setDistChannel(userMap.getString("VTWEG"));
	    		erpUserContext.setDivision(userMap.getString("SPART"));
	    		erpUserContext.setCustomerGrp5(userMap.getString("KVGR5"));
	    		erpUserContext.setCustomerType(userMap.getString("TYPE"));

	    		UserAuthenticationUtils.getUserDetails().setErpUserContext(erpUserContext);

	    		ErpDataMap erpDataMap = new ErpDataMap(userMap);
				erpDataMap.put("LANG", lang);

				return erpDataMap;
	        }
		}
		else
		{
			ErpDataMap erpDataMap = new ErpDataMap();

			erpDataMap.put("BUKRS", erpUserContext.getCorpNo());
			erpDataMap.put("BNAME", erpUserContext.getUsername());
			erpDataMap.put("KUNNR", erpUserContext.getCustomerNo());
			erpDataMap.put("NAME1", erpUserContext.getCustomerName());
			erpDataMap.put("VKORG", erpUserContext.getSalesOrg());
			erpDataMap.put("VTWEG", erpUserContext.getDistChannel());
			erpDataMap.put("SPART", erpUserContext.getDivision());
			erpDataMap.put("KVGR5", erpUserContext.getCustomerGrp5());
			erpDataMap.put("TYPE", erpUserContext.getCustomerType());
			erpDataMap.put("LANG", lang);

			return erpDataMap;
		}

		return null;
	}
}
