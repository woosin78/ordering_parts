package org.jwebppy.portal.iv.common.web;

import org.jwebppy.portal.common.web.PortalGeneralController;

public class IvGeneralController extends PortalGeneralController
{
//	@Autowired
//	private IvGeneralService ivGeneralService;

	/*
	public ErpDataMap getErpUserInfo()
	{
		ErpUserContext erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();
		String lang = CmStringUtils.defaultString(UserAuthenticationUtils.getUserDetails().getLanguage(), Locale.ENGLISH).toUpperCase();

		if (erpUserContext == null)
		{
	        RfcResponse response = ivGeneralService.getErpUserInfo(getUsername());
	        DataList userList = response.getTable("T_USER");

	        if (CollectionUtils.isNotEmpty(userList))
	        {
	    		erpUserContext = new PartsErpUserContext();

	    		DataMap userMap = (DataMap)userList.get(0);

	    		erpUserContext.setCorp(userMap.getString("BUKRS"));
	    		erpUserContext.setUsername(userMap.getString("BNAME").toUpperCase());
	    		erpUserContext.setCustCode(userMap.getString("KUNNR"));
	    		erpUserContext.setCustName(userMap.getString("NAME1"));
	    		erpUserContext.setSalesOrg(userMap.getString("VKORG"));
	    		erpUserContext.setDistChl(userMap.getString("VTWEG"));
	    		erpUserContext.setDivision(userMap.getString("SPART"));

	    		UserAuthenticationUtils.getUserDetails().setErpUserContext(erpUserContext);

	    		ErpDataMap erpDataMap = new ErpDataMap(userMap);
				erpDataMap.put("LANG", lang);

				return erpDataMap;
	        }
		}
		else
		{
			ErpDataMap erpDataMap = new ErpDataMap();

			erpDataMap.put("BUKRS", erpUserContext.getCorp());
			erpDataMap.put("BNAME", erpUserContext.getUsername());
			erpDataMap.put("KUNNR", erpUserContext.getCustCode());
			erpDataMap.put("NAME1", erpUserContext.getCustName());
			erpDataMap.put("VKORG", erpUserContext.getSalesOrg());
			erpDataMap.put("VTWEG", erpUserContext.getDistChl());
			erpDataMap.put("SPART", erpUserContext.getDivision());
			erpDataMap.put("LANG", lang);

			return erpDataMap;
		}

		return null;
	}
	*/
}
