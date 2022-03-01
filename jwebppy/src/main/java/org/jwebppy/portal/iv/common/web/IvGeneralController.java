package org.jwebppy.portal.iv.common.web;

import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.common.web.PortalGeneralController;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.jwebppy.portal.iv.hq.parts.common.PartsErpUserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public class IvGeneralController extends PortalGeneralController
{
	@Autowired
	private IvGeneralService ivGeneralService;

	protected ErpDataMap getErpUserInfo(String corpCode, String moduleCode)
	{
		ErpUserContext erpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();
		String lang = CmStringUtils.defaultString(UserAuthenticationUtils.getUserDetails().getLanguage(), Locale.ENGLISH).toUpperCase();

		if (erpUserContext == null)
		{
	        RfcResponse response = ivGeneralService.getErpUserInfo(getUsername(), corpCode, moduleCode);
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
	    		erpUserContext.setCustGrp1(userMap.getString("KVGR1"));
	    		erpUserContext.setCustGrp2(userMap.getString("KVGR2"));
	    		erpUserContext.setCustGrp3(userMap.getString("KVGR3"));
	    		erpUserContext.setCustGrp4(userMap.getString("KVGR4"));
	    		erpUserContext.setCustGrp5(userMap.getString("KVGR5"));

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
			erpDataMap.put("KVGR1", erpUserContext.getCustGrp1());
			erpDataMap.put("KVGR2", erpUserContext.getCustGrp2());
			erpDataMap.put("KVGR3", erpUserContext.getCustGrp3());
			erpDataMap.put("KVGR4", erpUserContext.getCustGrp4());
			erpDataMap.put("KVGR5", erpUserContext.getCustGrp5());
			erpDataMap.put("LANG", lang);

			return erpDataMap;
		}

		return null;
	}

	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("BASE_PATH", IvCommonVo.REQUEST_PATH);
	}
}
