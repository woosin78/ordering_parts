package org.jwebppy.platform.mgmt.user.web;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.Link;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.form.InputHidden;
import org.jwebppy.platform.core.web.ui.dom.form.Select;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.layout.PlatformLayoutBuildUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.conn_resource.dto.SapConnResourceDto;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindType;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;

import com.ibm.icu.util.TimeZone;

public class UserGroupLayoutBuilder
{
	public static Document pageableList(PageableList<UserGroupDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Name", "three wide");
		thTr.addTextTh("Description", "four wide");
		thTr.addTextTh("Language(Allowable / Default)", "three wide");
		thTr.addTextTh("Default Credentials Policy", "two wide");
		thTr.addTextTh("SAP Connection", "two wide");
		thTr.addTextTh("User", "one wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		for (UserGroupDto userGroup : ListUtils.emptyIfNull(pageableList.getList()))
		{
			int userCount = userGroup.getUserCount();

			Tr tbTr = new Tr();

			if (userCount == 0)
			{
				tbTr.addDataKeyCheckboxTd("ugSeq", userGroup.getUgSeq());
			}
			else
			{
				tbTr.addEmptyTd();
			}

			CredentialsPolicyDto credentialsPolicy = userGroup.getCredentialsPolicy();
			SapConnResourceDto sapConnecConnResource = userGroup.getSapConnResource();

			tbTr.addDataKeyLinkTd(userGroup.getName(), userGroup.getUgSeq());
			tbTr.addTextTd(userGroup.getDescription());
			tbTr.addTextTd(userGroup.getDisplayLangKind() + " / " + userGroup.getDisplayDefLang());
			tbTr.addDataKeyLinkTd(credentialsPolicy.getName(), credentialsPolicy.getCpSeq());
			tbTr.addDataKeyLinkTd(sapConnecConnResource.getName(), sapConnecConnResource.getScrSeq());
			tbTr.addDataKeyLinkTd(userCount, userGroup.getUgSeq());

			tbody.addTr(tbTr);
		}

		Document document = new Document();
		document.addElement(new Table(thead, tbody, pageableList));

		return document;
	}

	public static Document view(UserGroupDto userGroup)
	{
		SapConnResourceDto sapConnResource = userGroup.getSapConnResource();

		Link loSapConnResource = null;

		if (sapConnResource.isNotEmpty())
		{
			loSapConnResource = new Link(sapConnResource.getName() + " - " + sapConnResource.getDescription());
			loSapConnResource.setClass("sap-conn-resource");
			loSapConnResource.setKey(sapConnResource.getScrSeq());
		}

		CredentialsPolicyDto credentialsPolicy = userGroup.getCredentialsPolicy();

		Link loCredentialsPolicy = new Link(credentialsPolicy.getName() + " - " + credentialsPolicy.getDescription());
		loCredentialsPolicy.setClass("credentials-policy");
		loCredentialsPolicy.setKey(credentialsPolicy.getCpSeq());

		Link loUser = new Link(Integer.toString(userGroup.getUserCount()));
		loUser.setClass("user");
		loUser.setKey(userGroup.getUgSeq());

		StringBuilder langKind = new StringBuilder();
		String[] langKinds = CmStringUtils.split(userGroup.getLangKind(), PlatformConfigVo.DELIMITER);

		if (langKinds != null)
		{
			for (int i=0, length=langKinds.length; i<length; i++)
			{
				langKind.append(new Locale(langKinds[i]).getDisplayLanguage());

				if (i < length-1)
				{
					langKind.append(", ");
				}
			}
		}

		Document document = new Document();
		document.addDefaultLabelText("Name", userGroup.getName());
		document.addDefaultLabelText("Description", userGroup.getDescription());
		document.addDefaultLabelText("Date Format (Back-End)", userGroup.getDateFormat1());
		document.addDefaultLabelText("Time Format (Back-End)", userGroup.getTimeFormat1());
		document.addDefaultLabelText("Date Format (Front-End)", userGroup.getDateFormat2());
		document.addDefaultLabelText("Time Format (Front-End)", userGroup.getTimeFormat2());
		document.addDefaultLabelText("Default Country", userGroup.getDisplayCountry());
		document.addDefaultLabelText("Default Timezone", userGroup.getDisplayTimezone());
		document.addDefaultLabelText("Currency Format", userGroup.getCurrencyFormat());
		document.addDefaultLabelText("Weight Format", userGroup.getWeightFormat());
		document.addDefaultLabelText("Quantity Format", userGroup.getQtyFormat());
		document.addDefaultLabelText("Default Credentials Policy", loCredentialsPolicy);
		document.addDefaultLabelText("Allowable Languages", langKind);
		document.addDefaultLabelText("Default Language", userGroup.getDisplayDefLang());
		document.addDefaultLabelText("SAP Connection Resource", loSapConnResource);
		document.addDefaultLabelText("Users", loUser);
		document.addDefaultLabelText("Reg. Username", userGroup.getRegUsername());
		document.addDefaultLabelText("Reg. Date", userGroup.getDisplayRegDate());
		document.addDefaultLabelText("Mod. Username", userGroup.getModUsername());
		document.addDefaultLabelText("Mod. Date", userGroup.getDisplayModDate());
		document.addElement(new InputHidden("userCount", userGroup.getUserCount()));

		return document;
	}

	public static Document write(UserGroupDto userGroup, List<SapConnResourceDto> sapConnResources, List<CredentialsPolicyDto> credentialPolicies, String mode)
	{
		Document document = new Document();

		InputHidden loUgSeq = new InputHidden("ugSeq", userGroup.getUgSeq());
		loUgSeq.setId("ugSeq");

		Element loName = null;

		if (CmStringUtils.equals(mode, "copy") || userGroup.getUserCount() == 0)
		{
			loName = new Input("name", userGroup.getName());
			loName.setRequired(true);
			loName.setLabel("Name");
		}
		else
		{
			document.add(PlatformLayoutBuildUtils.defaultLabelText("Name", userGroup.getName()));

			loName = new InputHidden("name", userGroup.getName());
		}

		Input loDescription = new Input("description", userGroup.getDescription());
		loDescription.setLabel("Description");

		Input loDateFormat1 = new Input("dateFormat1", userGroup.getDateFormat1());
		loDateFormat1.setLabel("Date Format(Back-End)");
		loDateFormat1.setRequired(true);

		Input loTimeFormat1 = new Input("timeFormat1", userGroup.getTimeFormat1());
		loTimeFormat1.setLabel("Time Format(Back-End)");
		loTimeFormat1.setRequired(true);

		Input loDateFormat2 = new Input("dateFormat2", userGroup.getDateFormat2());
		loDateFormat2.setLabel("Date Format(Front-End)");
		loDateFormat2.setRequired(true);

		Input loTimeFormat2 = new Input("timeFormat2", userGroup.getTimeFormat2());
		loTimeFormat2.setLabel("Time Format(Front-End)");
		loTimeFormat2.setRequired(true);

		/* Country */
		Select loCountry = new Select("country");
		loCountry.setLabel("Default Country");
		loCountry.setRequired(true);
		loCountry.setValue(CmStringUtils.defaultIfEmpty(userGroup.getCountry(), PlatformCommonVo.DEFAULT_COUNTRY));

		String[] locales = Locale.getISOCountries();

		for (String loCountryCode : locales)
		{
			Locale locale = new Locale("", loCountryCode);

			loCountry.addOption(locale.getCountry(), locale.getDisplayCountry());
		}

		/* Timezone Format */
		Select loTimezone = new Select("timezone");
		loTimezone.setLabel("Timezone");
		loTimezone.setRequired(true);
		loTimezone.setValue(CmStringUtils.defaultIfEmpty(userGroup.getTimezone(), PlatformCommonVo.DEFAULT_TIMEZONE));

		String[] ids = TimeZone.getAvailableIDs(userGroup.getCountry());

		for (String id : ids)
		{
			loTimezone.addOption(id, id + ", " + TimeZone.getTimeZone(id).getDisplayName());
		}

		/* Currency Format */
		Input loCurrencyFormat = new Input("currencyFormat", userGroup.getCurrencyFormat());
		loCurrencyFormat.setLabel("Currency Format");
		loCurrencyFormat.setRequired(true);

		/* Weight Format */
		Input loWeightFormat = new Input("weightFormat", userGroup.getWeightFormat());
		loWeightFormat.setLabel("Weight Format");
		loWeightFormat.setRequired(true);

		/* Quantity Format */
		Input loQtyFormat = new Input("qtyFormat", userGroup.getQtyFormat());
		loQtyFormat.setLabel("Quantity Format");
		loQtyFormat.setRequired(true);

		/* Credential Policy */
		Select loCredentialPolicy = new Select("cpSeq");
		loCredentialPolicy.setLabel("Credential Policy");
		loCredentialPolicy.setRequired(true);

		CredentialsPolicyDto credentialsPolicy = userGroup.getCredentialsPolicy();

		if (credentialsPolicy != null)
		{
			loCredentialPolicy.setValue(credentialsPolicy.getCpSeq());
		}

		for (CredentialsPolicyDto credentialPolicy: ListUtils.emptyIfNull(credentialPolicies))
		{
			loCredentialPolicy.addOption(credentialPolicy.getCpSeq(), credentialPolicy.getName());
		}

		/* Allowable Language */
		Select loLangKindFields = new Select("langKind");
		loLangKindFields.setLabel("Allowable Language");
		loLangKindFields.setRequired(true);
		loLangKindFields.addAttribute("multiple", "");

		String langKind = userGroup.getLangKind();
		langKind = (CmStringUtils.isEmpty(langKind)) ? PlatformCommonVo.DEFAULT_LANGUAGE: langKind;

		for (LangKindType langKindType: LangKindType.values())
		{
			String value = langKindType.name();

			loLangKindFields.addOption(value, langKindType.getType(), CmStringUtils.contains(langKind, value));
		}

		/* Default Language */
		Select loDefLangFields = new Select("defLang");
		loDefLangFields.setLabel("Default Language");
		loDefLangFields.setRequired(true);
		loDefLangFields.setValue(userGroup.getDefLang());

		for (LangKindType langKindType: LangKindType.values())
		{
			String value = langKindType.name();

			if (CmStringUtils.contains(langKind, value))
			{
				loDefLangFields.addOption(value, langKindType.getType());
			}
		}

		/* SAP Connection Resource */
		Select loSapConnResource = new Select("scrSeq");
		loSapConnResource.setLabel("SAP Connection Resource");
		loSapConnResource.setValue(userGroup.getSapConnResource().getScrSeq());

		for (SapConnResourceDto sapConnResource: ListUtils.emptyIfNull(sapConnResources))
		{
			loSapConnResource.addOption(sapConnResource.getScrSeq(), sapConnResource.getName());
		}

		document.addElement(loUgSeq);
		document.addElement(loName);
		document.addElement(loDescription);
		document.addElement(loDateFormat1);
		document.addElement(loTimeFormat1);
		document.addElement(loDateFormat2);
		document.addElement(loTimeFormat2);
		document.addElement(loCountry);
		document.addElement(loTimezone);
		document.addElement(loCurrencyFormat);
		document.addElement(loWeightFormat);
		document.addElement(loQtyFormat);
		document.addElement(loCredentialPolicy);
		document.addElement(loLangKindFields);
		document.addElement(loDefLangFields);
		document.addElement(loSapConnResource);

		return document;
	}
}
