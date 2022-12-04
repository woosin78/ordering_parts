package org.jwebppy.platform.core.security.authentication.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.AuthenticationType;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class PlatformUserDetails implements UserDetails
{
	private static final long serialVersionUID = 5119166105899539942L;

	private Integer useq;
	private String name;
	private String username;
	private String password;
	private String fgAccountLocked;
	private String fgPasswordLocked;
	private LocalDateTime fromValid;
	private LocalDateTime toValid;
	private String language;
	private String dateFormat1;//Back-End
	private String timeFormat1;//Back-End
	private String dateFormat2;//Front-End
	private String timeFormat2;//Front-End
	private String timezone;
	private String currencyFormat;
	private String weightFormat;
	private String qtyFormat;
	private AuthenticationType authenticationType;
	private String realUsername;

	private List<CItemDto> citems;

	private ErpUserContext erpUserContext;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		if (CollectionUtils.isNotEmpty(citems))
		{
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();

	        for (CItemDto citem : citems)
	        {
	        	authorities.add(new SimpleGrantedAuthority("ROLE_" + citem.getName()));
	        }

	        return authorities;
		}

		return null;
	}

	@Override
	public String getPassword()
	{
		return password;
	}

	@Override
	public String getUsername()
	{
		return username;
	}

	//Indicates whether the user's account has expired.
	@Override
	public boolean isAccountNonExpired()
	{
		if (fromValid != null && toValid != null)
		{
			LocalDateTime now = LocalDateTime.now();

			if ((now.isEqual(fromValid) || now.isAfter(fromValid)) && now.isBefore(toValid))
			{
				return true;
			}
		}

		return false;
	}

	//Indicates whether the user is locked or unlocked.
	@Override
	public boolean isAccountNonLocked()
	{
		if (CmStringUtils.equals(PlatformCommonVo.NO, fgAccountLocked))
		{
			return true;
		}

		return false;
	}

	//Indicates whether the user's credentials (password) has expired.
	@Override
	public boolean isCredentialsNonExpired()
	{
		if (CmStringUtils.equals(PlatformCommonVo.NO, fgPasswordLocked))
		{
			return true;
		}

		return false;
	}

	//Indicates whether the user is enabled or disabled.
	@Override
	public boolean isEnabled()
	{
		if (isAccountNonExpired() && isAccountNonLocked() && isCredentialsNonExpired())
		{
			return true;
		}

		return false;
	}

	public String getLanguage()
	{
		return CmStringUtils.lowerCase(language);
	}

	public String getDateFormat1()
	{
		return CmStringUtils.defaultIfEmpty(dateFormat1, PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}

	public String getTimeFormat1()
	{
		return CmStringUtils.defaultIfEmpty(timeFormat1, PlatformCommonVo.DEFAULT_TIME_FORMAT);
	}

	public String getDateTimeFormat1()
	{
		return getDateFormat1() + " " + getTimeFormat1();
	}

	public String getDateFormat2()
	{
		return CmStringUtils.defaultIfEmpty(dateFormat2, PlatformCommonVo.DEFAULT_DATE_FORMAT);
	}

	public String getTimeFormat2()
	{
		return CmStringUtils.defaultIfEmpty(timeFormat2, PlatformCommonVo.DEFAULT_TIME_FORMAT);
	}

	public String getDateTimeFormat2()
	{
		return getDateFormat2() + " " + getTimeFormat2();
	}

	public String getCurrencyFormat()
	{
		return CmStringUtils.defaultIfEmpty(currencyFormat, PlatformCommonVo.DEFAULT_CURRENCY_FORMAT);
	}

	public String getWeightFormat()
	{
		return CmStringUtils.defaultIfEmpty(weightFormat, PlatformCommonVo.DEFAULT_WEIGHT_FORMAT);
	}

	public String getQtyFormat()
	{
		return CmStringUtils.defaultIfEmpty(qtyFormat, PlatformCommonVo.DEFAULT_QTY_FORMAT);
	}
}
