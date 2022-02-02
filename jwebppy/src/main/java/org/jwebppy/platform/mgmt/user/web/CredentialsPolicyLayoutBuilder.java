package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
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
	public static Document pageableList(PageableList<CredentialsPolicyDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Name", "four wide");
		thTr.addTextTh("Description", "nine wide");
		thTr.addTextTh("Use", "one wide");
		thTr.addTextTh("Default", "one wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<CredentialsPolicyDto> credentialsPolicies = ListUtils.emptyIfNull(pageableList.getList());

		for (CredentialsPolicyDto credentialsPolicy: credentialsPolicies)
		{
			String fgDefault = credentialsPolicy.getFgDefault();

			Tr tbTr = new Tr();

			if (CmStringUtils.equals(fgDefault, PlatformCommonVo.YES))
			{
				tbTr.addEmptyTd();
			}
			else
			{
				tbTr.addDataKeyCheckboxTd("cpSeq", credentialsPolicy.getCpSeq());
			}

			tbTr.addDataKeyLinkTd(credentialsPolicy.getName(), credentialsPolicy.getCpSeq());
			tbTr.addTextTd(credentialsPolicy.getDescription());
			tbTr.addTextTd(credentialsPolicy.getFgUse());
			tbTr.addTextTd(fgDefault);

			tbody.addTr(tbTr);
		}

		Document document = new Document();
		document.add(new Table(thead, tbody, pageableList));

		return document;
	}
}
