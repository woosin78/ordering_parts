package org.jwebppy.platform.mgmt.user.dto;

import java.util.List;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class UserSearchDto extends MgmtGeneralDto implements IPagination
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

	public UserSearchDto(Integer uSeq)
	{
		this.uSeq = uSeq;
	}

	public UserSearchDto(String username)
	{
		this.username = username;
	}
}
