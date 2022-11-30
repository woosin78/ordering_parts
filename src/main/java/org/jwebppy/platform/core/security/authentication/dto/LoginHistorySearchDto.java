package org.jwebppy.platform.core.security.authentication.dto;

import java.time.LocalDateTime;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

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
public class LoginHistorySearchDto extends GeneralDto implements IPagination
{
	private static final long serialVersionUID = 1939202716125055543L;

	private Integer uSeq;
	private String username;
	private String fgResult;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private String query;
}
