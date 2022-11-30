package org.jwebppy.platform.mgmt.i18n.dto;

import java.util.List;

import org.jwebppy.platform.core.web.ui.pagination.IPagination;
import org.jwebppy.platform.mgmt.common.dto.MgmtGeneralDto;

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
public class LangSearchDto extends MgmtGeneralDto implements IPagination
{
	private static final long serialVersionUID = 6172725324377871037L;

	private Integer lSeq;
	private String basename;
	private LangType type;
	private String query;
	private String seq;
	private String code;//Language code. etc)ko, en
	private String messageCode;
	private String from;
	private Integer lkSeq;
	private List<Integer> lSeqs;
}
