package org.jwebppy.platform.mgmt.i18n.dto;

import org.jwebppy.platform.core.dto.GeneralDto;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangSearchDto extends GeneralDto implements IPagination
{
	private Integer lSeq;
	private String basename;
	private String type;
	private String query;
	private String seq;
	private String code;//Language code. etc)ko, en
	private String messageCode;
	private String from;
}
