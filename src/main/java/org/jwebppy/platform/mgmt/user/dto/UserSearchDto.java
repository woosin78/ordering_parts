package org.jwebppy.platform.mgmt.user.dto;

import java.util.List;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserSearchDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = -6452889800109776370L;

	private Integer uSeq;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String country;
	private String timezone;
	private String query;
	private Integer cSeq;
	private Integer ugSeq;
	private List<String> usernames;

	public UserSearchDto() {}

	public UserSearchDto(Integer uSeq)
	{
		this.uSeq = uSeq;
	}

	public UserSearchDto(String username)
	{
		this.username = username;
	}
}
