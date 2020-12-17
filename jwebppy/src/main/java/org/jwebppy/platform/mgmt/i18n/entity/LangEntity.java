package org.jwebppy.platform.mgmt.i18n.entity;

import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.entity.GeneralEntity;
import org.jwebppy.platform.core.web.ui.pagination.IPagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LangEntity extends GeneralEntity implements IPagination
{
	private static final long serialVersionUID = -4634138279238831478L;
	private Integer lSeq;
	private String type;
	private String seq;
	private String fgDelete = PlatformCommonVo.NO;
	private List<LangDetailEntity> langDetails;
}
