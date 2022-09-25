package org.jwebppy.platform.mgmt.user.web;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;

public class LoginHistoryLayoutBuilder
{
	public static Document listLoginHistory(List<LoginHistoryDto> loginHistories)
	{
		Tr thTr = new Tr();
		thTr.addTextTh("Login Time(Local Time)", "two wide");
		thTr.addTextTh("Username", "one wide");
		thTr.addTextTh("IP", "two wide");
		thTr.addTextTh("Referer", "three wide");
		thTr.addTextTh("User Agent", "five wide");
		thTr.addTextTh("Auth. Type", "one wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		if (CollectionUtils.isNotEmpty(loginHistories))
		{
			for (LoginHistoryDto loginHistory : loginHistories)
			{
				String regDate = loginHistory.getDisplayRegDate();

				if (CmStringUtils.isNotEmpty(loginHistory.getTimezone()))
				{
					regDate += "<br/>(" + loginHistory.getDisplayZonedRegDate() + ", " + loginHistory.getTimezone() + ")";
				}

				Tr tbTr = new Tr();
				tbTr.addTextTd(regDate);
				tbTr.addTextTd(loginHistory.getUsername());
				tbTr.addTextTd(loginHistory.getIp());
				tbTr.addTextTd(loginHistory.getReferer());
				tbTr.addTextTd(loginHistory.getUserAgent());
				tbTr.addTextTd(loginHistory.getAuthenticationType().getType());

				if (CmStringUtils.equals(loginHistory.getFgResult(), PlatformCommonVo.NO))
				{
					tbTr.setClass("error");
				}

				tbody.addTr(tbTr);
			}
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody));

		return document;
	}

	public static Document pageableList(PageableList<LoginHistoryDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addTextTh("Login Time(Local Time)", "two wide");
		thTr.addTextTh("Username", "two wide");
		thTr.addTextTh("IP", "two wide");
		thTr.addTextTh("Referer", "five wide");
		thTr.addTextTh("User Agent", "six wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		List<LoginHistoryDto> loginHistories = ListUtils.emptyIfNull(pageableList.getList());

		for (LoginHistoryDto loginHistory : loginHistories)
		{
			String regDate = loginHistory.getDisplayRegDate();

			if (CmStringUtils.isNotEmpty(loginHistory.getTimezone()))
			{
				 regDate += "<br/>(" + loginHistory.getDisplayZonedRegDate() + ")";
			}

			Tr tbTr = new Tr();
			tbTr.addTextTd(regDate);
			tbTr.addTextTd(loginHistory.getUsername());
			tbTr.addTextTd(loginHistory.getIp());
			tbTr.addTextTd(loginHistory.getReferer());
			tbTr.addTextTd(loginHistory.getUserAgent());

			if (CmStringUtils.equals(loginHistory.getFgResult(), PlatformCommonVo.NO))
			{
				tbTr.setClass("error");
			}

			tbody.addTr(tbTr);
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody, pageableList));

		return document;
	}
}
