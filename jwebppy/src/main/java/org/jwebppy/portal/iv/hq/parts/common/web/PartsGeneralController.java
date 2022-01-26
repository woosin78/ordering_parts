package org.jwebppy.portal.iv.hq.parts.common.web;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.hq.common.web.HqGeneralController;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpDataMap;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpUserContext;
import org.jwebppy.portal.iv.hq.parts.common.service.PartsGeneralService;
import org.springframework.beans.factory.annotation.Autowired;

public class PartsGeneralController extends HqGeneralController
{
	@Autowired
	private PartsGeneralService partsGeneralService;

	public PartsErpDataMap getErpUserInfo()
	{
		PartsErpUserContext erpUserContext = (PartsErpUserContext)UserAuthenticationUtils.getUserDetails().getErpUserContext();
		String lang = CmStringUtils.defaultString(UserAuthenticationUtils.getUserDetails().getLanguage(), Locale.ENGLISH).toUpperCase();

		if (erpUserContext == null || CmStringUtils.isEmpty(erpUserContext.getCorpName()))
		{
	        RfcResponse response = partsGeneralService.getErpUserInfo(getUsername());
	        DataList userList = response.getTable("T_USER");

	        if (CollectionUtils.isNotEmpty(userList))
	        {
	    		erpUserContext = new PartsErpUserContext();

	    		DataMap userMap = (DataMap)userList.get(0);

	    		userMap.put("TYPE", response.getString("O_TYPE"));

	    		erpUserContext.setCorp(userMap.getString("BUKRS"));
	    		erpUserContext.setUsername(userMap.getString("BNAME").toUpperCase());
	    		erpUserContext.setCustCode(userMap.getString("KUNNR"));
	    		erpUserContext.setCustName(userMap.getString("NAME1"));
	    		erpUserContext.setSalesOrg(userMap.getString("VKORG"));
	    		erpUserContext.setDistChl(userMap.getString("VTWEG"));
	    		erpUserContext.setDivision(userMap.getString("SPART"));
	    		erpUserContext.setCustGrp5(userMap.getString("KVGR5"));
	    		erpUserContext.setCustType(userMap.getString("TYPE"));

	    		UserAuthenticationUtils.getUserDetails().setErpUserContext(erpUserContext);

	    		PartsErpDataMap erpDataMap = new PartsErpDataMap(userMap);
				erpDataMap.put("LANG", lang);

				return erpDataMap;
	        }
		}
		else
		{
			PartsErpDataMap erpDataMap = new PartsErpDataMap();

			erpDataMap.put("BUKRS", erpUserContext.getCorp());
			erpDataMap.put("BNAME", erpUserContext.getUsername());
			erpDataMap.put("KUNNR", erpUserContext.getCustCode());
			erpDataMap.put("NAME1", erpUserContext.getCustName());
			erpDataMap.put("VKORG", erpUserContext.getSalesOrg());
			erpDataMap.put("VTWEG", erpUserContext.getDistChl());
			erpDataMap.put("SPART", erpUserContext.getDivision());
			erpDataMap.put("KVGR5", erpUserContext.getCustGrp5());
			erpDataMap.put("TYPE", erpUserContext.getCustType());
			erpDataMap.put("LANG", lang);

			return erpDataMap;
		}

		return null;
	}

	//KRW, JPY 는 가격에 100을 곱해 줌
	protected void makePriceByCurrency(List<Map<String, Object>> list, String[] keys, String currencyKey, String[] targetCurrencies, double value)
	{
		for (Map<String, Object> map: list)
		{
			String currency = (String)map.get(currencyKey);

			if (ArrayUtils.contains(targetCurrencies, currency))
			{
				for (int i=0, length=keys.length; i<length; i++)
				{
					if (CmStringUtils.isNotEmpty(map.get(keys[i])))
					{
						try
						{
							map.put(keys[i], Double.parseDouble(map.get(keys[i]).toString()) * value);
						}
						catch (NumberFormatException e) {}
					}
				}
			}
		}
	}
}
