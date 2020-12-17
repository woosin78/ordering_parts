package org.jwebppy.platform.mgmt.user.dto;

import java.io.Serializable;
import java.util.Locale;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.util.CmStringUtils;

import com.ibm.icu.util.TimeZone;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserContactInfoDto extends GeneralDto implements Serializable
{
	private static final long serialVersionUID = -809245644363657970L;

	private Integer uSeq;
	private String email;
	private String tel1;
	private String tel2;
	private String tel3;
	private String mobile1;
	private String mobile2;
	private String mobile3;
	private String fax1;
	private String fax2;
	private String fax3;
	private String zipcode;
	private String street;
	private String city;
	private String state;
	private String country;
	private String timezone;

	public String getTel()
	{
		return CmStringUtils.trimToEmpty(tel1) + "-" + CmStringUtils.trimToEmpty(tel2) + "-" + CmStringUtils.trimToEmpty(tel3);
	}

	public String getMobile()
	{
		return CmStringUtils.trimToEmpty(mobile1) + "-" + CmStringUtils.trimToEmpty(mobile2) + "-" + CmStringUtils.trimToEmpty(mobile3);
	}

	public String getFax()
	{
		return CmStringUtils.trimToEmpty(fax1) + "-" + CmStringUtils.trimToEmpty(fax2) + "-" + CmStringUtils.trimToEmpty(fax3);
	}

	public String getDisplayCountry()
	{
		if (CmStringUtils.isNotEmpty(country))
		{
			return new Locale("", country).getDisplayName();
		}

		return country;
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
}
