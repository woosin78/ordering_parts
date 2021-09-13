package org.jwebppy.platform.mgmt.i18n.resource;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;

public class DbMessageSource extends AbstractMessageSource
{
	@Autowired
	private LangService langService;

	@Override
	protected MessageFormat resolveCode(String key, Locale locale)
	{
		String[] codes = CmStringUtils.split(key, "_");

		String lang = locale.getLanguage();

		if (UserAuthenticationUtils.isLogin())
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
						if (CmStringUtils.equals(langKind.getFgDefault(), PlatformCommonVo.YES))
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

			return new MessageFormat(CmStringUtils.trimToEmpty(text), locale);
		}

		return new MessageFormat("", locale);
	}
}
