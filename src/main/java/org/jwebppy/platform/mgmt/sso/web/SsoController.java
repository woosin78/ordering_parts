package org.jwebppy.platform.mgmt.sso.web;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.mgmt.common.web.MgmtGeneralController;
import org.jwebppy.platform.mgmt.sso.uitils.StringEncrypter;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/sso")
public class SsoController extends MgmtGeneralController
{
	protected String encrypt(String key, String iv, String delimeter)
	{
		try
		{
			return new StringEncrypter(key, iv).encrypt(getUsername().toUpperCase() + delimeter + CmDateFormatUtils.now(null, IvCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	protected String decrypt(String key, String iv)
	{
		try
		{
			return new StringEncrypter(key, iv).decrypt(key);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
