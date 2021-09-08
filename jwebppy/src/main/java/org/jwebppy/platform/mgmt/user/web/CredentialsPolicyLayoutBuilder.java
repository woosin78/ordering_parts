package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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
		thTr.addTextTh("User Group", "two wide");
		thTr.addTextTh("Name", "two wide");
		thTr.addTextTh("Description", "five wide");
		thTr.addTextTh("Use", "one wide");
		thTr.addTextTh("Default", "one wide");
		thTr.addTextTh("Reg.Date", "two wide");
		thTr.addTextTh("Reg.Username", "two wide");

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
				tbTr.addTextTd(credentialsPolicy.getUserGroup().getName());
				tbTr.addDataKeyLinkTd(credentialsPolicy.getName(), credentialsPolicy.getCpSeq());
				tbTr.addTextTd(credentialsPolicy.getDescription());
				tbTr.addTextTd(credentialsPolicy.getFgUse());
				tbTr.addTextTd(credentialsPolicy.getFgDefault());
				tbTr.addTextTd(credentialsPolicy.getDisplayRegDate());
				tbTr.addTextTd(credentialsPolicy.getRegUsername());

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
