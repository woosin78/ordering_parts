package org.jwebppy.platform.mgmt.i18n.mapper;

import java.util.List;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangSearchDto;
import org.jwebppy.platform.mgmt.i18n.entity.LangDetailEntity;
import org.jwebppy.platform.mgmt.i18n.entity.LangEntity;
import org.jwebppy.platform.mgmt.i18n.entity.LangKindEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LangMapper
{
	public int insertLang(LangEntity lang);
	public int insertLangDetail(LangDetailEntity langDetail);
	public int updateLang(LangEntity lang);
	public int updateLangDetail(LangDetailEntity langDetail);
	public int updateLangFgDelete(LangDto lang);
	public int updateLangDetailFgDelete(LangDto lang);
	public LangEntity findLang(LangDto lang);
	public List<LangEntity> findLangs(LangSearchDto langSearch);
	public LangDetailEntity findLangDetail(LangDetailDto langDetail);
	@NoLogging
	public List<LangDetailEntity> findLangDetails(LangSearchDto langSearch);
	@NoLogging
	public List<LangKindEntity> findLangKinds(LangKindDto langKind);
	@NoLogging
	public List<LangDetailEntity> findLangDetailsCItems(CItemSearchDto cItemSearch);
}
