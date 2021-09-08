package org.jwebppy.platform.core.security.authentication.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PlatformUserDetails implements UserDetails
{
	private static final long serialVersionUID = 5119166105899539942L;

	private Integer uSeq;
	private String name;
	private String username;
	private String password;
	private String fgAccountLocked;
	private String fgPasswordLocked;
	private LocalDateTime fromValid;
	private LocalDateTime toValid;
	private List<CItemDto> cItems;
	private String language;
	private Object erpUserContext;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		if (CollectionUtils.isNotEmpty(cItems))
		{
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();

	        for (CItemDto cItem : cItems)
	        {
	        	authorities.add(new SimpleGrantedAuthority("ROLE_" + cItem.getName()));
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
}
