package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;

public class CredentialsPolicyLayoutBuilder
{
	public static Document pageableList(PageableList<CredentialsPolicyDto> pageableList)
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
		List<CredentialsPolicyDto> credentialsPolicies = ListUtils.emptyIfNull(pageableList.getList());

		for (CredentialsPolicyDto credentialsPolicy: credentialsPolicies)
		{
			UserGroupDto userGroup = credentialsPolicy.getUserGroup();

			Tr tbTr = new Tr();

			tbTr.addDataKeyCheckboxTd("cpSeq", credentialsPolicy.getCpSeq());
			tbTr.addDataKeyLinkTd(userGroup.getName(), userGroup.getUgSeq());
			tbTr.addDataKeyLinkTd(credentialsPolicy.getName(), credentialsPolicy.getCpSeq());
			tbTr.addTextTd(credentialsPolicy.getDescription());
			tbTr.addTextTd(credentialsPolicy.getFgUse());
			tbTr.addTextTd(credentialsPolicy.getFgDefault());
			tbTr.addTextTd(credentialsPolicy.getDisplayRegDate());
			tbTr.addTextTd(credentialsPolicy.getRegUsername());

			tbody.addTr(tbTr);
		}

		Document document = new Document();
		document.add(new Table(thead, tbody, pageableList));

		return document;
	}
}
