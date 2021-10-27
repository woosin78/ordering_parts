package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.user.dto.CredentialsSecurityDto;

public class CredentialsSecurityLayoutBuilder
{
	public static Document pageableList(PageableList<CredentialsSecurityDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Name", "four wide");
		thTr.addTextTh("Description", "six wide");
		thTr.addTextTh("Use", "one wide");
		thTr.addTextTh("Default", "one wide");
		thTr.addTextTh("User Group", "three wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<CredentialsSecurityDto> credentialsSecurities = ListUtils.emptyIfNull(pageableList.getList());

		for (CredentialsSecurityDto credentialsSecurity: credentialsSecurities)
		{

		}

		Document document = new Document();
		document.add(new Table(thead, tbody, pageableList));

		return document;
	}
}
