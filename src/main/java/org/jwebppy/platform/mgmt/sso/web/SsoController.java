package org.jwebppy.platform.mgmt.sso.web;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmDateTimeUtils;
import org.jwebppy.platform.mgmt.common.web.MgmtGeneralController;
import org.jwebppy.platform.mgmt.sso.uitils.StringEncrypter;
import org.jwebppy.portal.iv.common.IvCommonVo;

public class SsoController extends MgmtGeneralController
{
	public String encrypt(String key, String iv, String delimeter)
	{
		try
		{
			return URLEncoder.encode(new StringEncrypter(key, iv).encrypt(getUsername().toUpperCase() + delimeter + CmDateFormatUtils.now(null, IvCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS)), "UTF-8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public String decrypt(String key, String iv, String token)
	{
		try
		{
			return new StringEncrypter(key, iv).decrypt(token);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public boolean isValidPeriod(String time)
	{
		try
		{
			LocalDateTime ssoTime = CmDateTimeUtils.toLocalDateTime(time, PlatformCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS);

			if (ssoTime.plusMinutes(1).compareTo(LocalDateTime.now()) > 0)
			{
				return true;
			}
		}
		catch (DateTimeParseException e) {}

		return false;
	}

	public static void main(String[] args)
	{
		try
		{
			System.err.println(URLEncoder.encode(new StringEncrypter("GPES", "everythingisok").encrypt("P_IVDO01".toUpperCase() + ":" + CmDateFormatUtils.now(null, IvCommonVo.DEFAULT_DATE_TIME_FORMAT_YYYYMMDDHHMMSS)), "UTF-8"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
