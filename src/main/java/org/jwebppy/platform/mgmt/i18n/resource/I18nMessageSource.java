package org.jwebppy.platform.mgmt.i18n.resource;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RegExUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;
import org.jwebppy.platform.core.util.CmArrayUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.support.AbstractMessageSource;

public class I18nMessageSource extends AbstractMessageSource
{
	@Autowired
	private LangService langService;

	@Override
	protected MessageFormat resolveCode(String key, Locale locale)
	{
		return new MessageFormat(getMessage(key, locale), locale);
	}

	public String getMessage(String key)
	{
		return getMessage(key, new Locale(UserAuthenticationUtils.getLanguage()));
	}

	public String getMessage(String key, Object[] replaceTexts)
	{
		String text = getMessage(key);

		for (int i=0, length=CmArrayUtils.nullToEmpty(replaceTexts).length; i<length; i++)
		{
			text = RegExUtils.replaceAll(text, "\\{" + i + "\\}", CmStringUtils.trimToEmpty(replaceTexts[i]));
		}

		return text;
	}

	@Cacheable(value = CacheConfig.LANG, key = "{#key, #locale}", unless="#result == null")
	public String getMessage(String key, Locale locale)
	{
		key = CmStringUtils.upperCase(key);

		String[] codes = CmStringUtils.split(key, "_");

		String lang = locale.toLanguageTag();

		if (UserAuthenticationUtils.isAuthenticated())
		{
			PlatformUserDetails platformUserDetails = UserAuthenticationUtils.getUserDetails();

			if (platformUserDetails != null)
			{
				lang = platformUserDetails.getLanguage();
			}

			locale = new Locale(lang);
		}

		if (codes != null && codes.length >= 3)
		{
			LangDetailDto langDetail = langService.getLangDetailByCode(key, lang);
			String text = null;

			if (langDetail != null)
			{
				text = langDetail.getText();
			}
			else
			{
				//Get default language
				List<LangKindDto> langKinds = langService.getLangKinds(new LangKindDto(codes[0]));
				String defaultLocale = null;

				if (CollectionUtils.isNotEmpty(langKinds))
				{
					for (LangKindDto langKind : langKinds)
					{
						if (CmStringUtils.equals(langKind.getFgDefault(), MgmtCommonVo.YES))
						{
							defaultLocale = langKind.getCode();
							break;
						}
					}
				}

				if (defaultLocale != null)
				{
					langDetail = langService.getLangDetailByCode(key, defaultLocale);

					if (langDetail != null)
					{
						text = langDetail.getText();
					}
				}
			}

			return CmStringUtils.trimToEmpty(text);
		}

		return CmStringUtils.EMPTY;
	}
}
