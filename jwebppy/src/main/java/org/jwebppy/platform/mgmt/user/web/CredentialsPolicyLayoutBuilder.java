package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;

public class CredentialsPolicyLayoutBuilder
{
	public static Document getList(PageableList<CredentialsPolicyDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Name", "two wide");
		thTr.addTextTh("Description", "three wide");
		thTr.addTextTh("Use", "one wide");
		thTr.addTextTh("Default", "one wide");
		thTr.addTextTh("Reg.Date", "two wide");
		thTr.addTextTh("Reg.Username", "two wide");
		thTr.addTextTh("Mod.Date", "two wide");
		thTr.addTextTh("Mod.Username", "two wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<CredentialsPolicyDto> credentialsPolicies = pageableList.getList();

		if (CollectionUtils.isNotEmpty(credentialsPolicies))
		{
			for (CredentialsPolicyDto credentialsPolicy: credentialsPolicies)
			{
				Tr tbTr = new Tr();

				tbTr.addDataKeyCheckboxTd("cpSeq", credentialsPolicy.getCpSeq());
				tbTr.addTextTd(credentialsPolicy.getName());
				tbTr.addTextTd(credentialsPolicy.getDescription());
				tbTr.addCheckboxTd("fgUse", credentialsPolicy.getCpSeq(), PlatformCommonVo.YES, CmStringUtils.defaultIfEmpty(credentialsPolicy.getFgUse(), PlatformCommonVo.NO));
				tbTr.addCheckboxTd("fgDefault", credentialsPolicy.getCpSeq(), PlatformCommonVo.YES, CmStringUtils.defaultIfEmpty(credentialsPolicy.getFgDefault(), PlatformCommonVo.NO));
				tbTr.addTextTd(credentialsPolicy.getDisplayRegDate());
				tbTr.addTextTd(credentialsPolicy.getRegUsername());
				tbTr.addTextTd(credentialsPolicy.getDisplayModDate());
				tbTr.addTextTd(credentialsPolicy.getModUsername());

				tbody.addTr(tbTr);
			}
		}

		Table table = new Table(pageableList);
		table.addThead(thead);
		table.addTbody(tbody);

		Document document = new Document();
		document.add(table);

		return document;
	}
}
