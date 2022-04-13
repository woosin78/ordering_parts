package org.jwebppy.platform.mgmt.user.dto;

import java.util.Locale;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;

import com.ibm.icu.util.TimeZone;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -532421241477305133L;

	private Integer ugSeq;
	private String name;
	private String description;
	private SapConnResourceDto sapConnResource;
	private String dateFormat1;
	private String timeFormat1;
	private String dateFormat2;
	private String timeFormat2;
	private String country;
	private String timezone;
	private String currencyFormat;
	private String langKind;
	private String defLang;
	private CredentialsPolicyDto credentialsPolicy;
	private int userCount;

	public UserGroupDto() {}

	public UserGroupDto(Integer ugSeq)
	{
		this.ugSeq = ugSeq;
	}

	public SapConnResourceDto getSapConnResource()
	{
		return (sapConnResource == null) ? new SapConnResourceDto() : sapConnResource;
	}

	public String getDisplayCountry()
	{
		if (CmStringUtils.isNotEmpty(country))
		{
			return new Locale("", country).getDisplayName();
		}

		return country;
	}

	public String getDisplayLangKind()
	{
		StringBuilder allowableLanguages = new StringBuilder();
		String[] langKinds = CmStringUtils.split(langKind, PlatformConfigVo.DELIMITER);

		for (int i=0, length=langKinds.length; i<length; i++)
		{
			allowableLanguages.append(new Locale(langKinds[i]).getDisplayLanguage());

			if (i < length-1)
			{
				allowableLanguages.append(", ");
			}
		}

		return allowableLanguages.toString();
	}

	public String getDisplayTimezone()
	{
		if (CmStringUtils.isNotEmpty(timezone))
		{
			TimeZone myTimezone = TimeZone.getTimeZone(timezone);

			return timezone + ", " + myTimezone.getDisplayName();
		}

		return timezone;
	}

	public String getDisplayDefLang()
	{
		if (CmStringUtils.isNotEmpty(defLang))
		{
			return new Locale(defLang).getDisplayLanguage();
		}

		return null;
	}
}