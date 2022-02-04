package org.jwebppy.portal.iv.hq.parts.common.web;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
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

		if (erpUserContext == null)
		{
	        RfcResponse response = partsGeneralService.getErpUserInfo();
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
	    		erpUserContext.setCustGrp5(userMap.getString("KVGR5"));

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
			erpDataMap.put("LANG", lang);

			return erpDataMap;
		}

		return null;
	}

	protected void calcPriceByCurrency(Object target, String[] keys, String currKey, String[] targets, double value)
	{
		if (target instanceof List)
		{
			List<Map<String, Object>> list = (List<Map<String, Object>>)target;

			if (CollectionUtils.isNotEmpty(list))
			{
				for (Map<String, Object> map: list)
				{
					calcValueByCurrency(map, keys, currKey, targets, value);
				}
			}

		}
		else if (target instanceof Map)
		{
			calcValueByCurrency((Map<String, Object>)target, keys, currKey, targets, value);
		}
	}

	protected void calcValueByCurrency(Map<String, Object> map, String[] keys, String currKey, String[] targets, double value)
	{
		if (MapUtils.isNotEmpty(map))
		{
			if (CmStringUtils.isEmpty(currKey) || ArrayUtils.contains(targets, CmStringUtils.trimToEmpty(map.get(currKey))))
			{
				Iterator<String> it = map.keySet().iterator();

				while (it.hasNext())
				{
					String key = it.next();

					if (ArrayUtils.contains(keys, key))
					{
						if (CmStringUtils.isNotEmpty(map.get(key)))
						{
							try
							{
								map.put(key, new BigDecimal(CmStringUtils.defaultIfEmpty(map.get(key), "0")).multiply(new BigDecimal(value)));
							}
							catch (NumberFormatException e) {}
						}
					}
				}
			}
		}
	}
}
