package org.jwebppy.platform.mgmt.user.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmDateFormatUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.dom.Document;
import org.jwebppy.platform.core.web.ui.dom.Element;
import org.jwebppy.platform.core.web.ui.dom.form.Checkbox;
import org.jwebppy.platform.core.web.ui.dom.form.Input;
import org.jwebppy.platform.core.web.ui.dom.form.InputPassword;
import org.jwebppy.platform.core.web.ui.dom.form.Select;
import org.jwebppy.platform.core.web.ui.dom.table.Table;
import org.jwebppy.platform.core.web.ui.dom.table.Tbody;
import org.jwebppy.platform.core.web.ui.dom.table.Th;
import org.jwebppy.platform.core.web.ui.dom.table.Thead;
import org.jwebppy.platform.core.web.ui.dom.table.Tr;
import org.jwebppy.platform.core.web.ui.layout.PlatformLayoutBuildUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserContactInfoDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.security.authentication.dto.LoginHistoryDto;

import com.ibm.icu.util.TimeZone;

public class UserLayoutBuilder
{
	public static Document getList(PageableList<UserDto> pageableList)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTextTh("Status", "one wide");
		thTr.addTextTh("Username", "two wide");
		thTr.addTextTh("Name", "three wide");
		thTr.addTextTh("E-mail", "three wide");
		thTr.addTextTh("Company", "three wide");
		thTr.addTextTh("Department", "three wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();
		List<UserDto> users = pageableList.getList();

		if (CollectionUtils.isNotEmpty(users))
		{
			for (UserDto user : users)
			{
				UserAccountDto userAccount = user.getUserAccount();
				UserContactInfoDto userContactInfo = user.getUserContactInfo();

				Tr tbTr = new Tr();
				tbTr.addDataKeyCheckboxTd("uSeq", user.getUSeq());
				tbTr.addTextTd(userAccount.getFgAccountLocked());
				tbTr.addTextTd(userAccount.getUsername());
				tbTr.addDataKeyLinkTd(user.getName(), user.getUSeq());
				tbTr.addTextTd(userContactInfo.getEmail());
				tbTr.addTextTd(user.getCompany());
				tbTr.addTextTd(user.getDepartment());

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

	public static Document getGeneralInfo(UserDto user)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Last Name", CmStringUtils.trimToEmpty(user.getLastName()));
		elementMap.put("First Name", CmStringUtils.trimToEmpty(user.getFirstName()));
		elementMap.put("Company", CmStringUtils.trimToEmpty(user.getCompany()));
		elementMap.put("Organization", CmStringUtils.trimToEmpty(user.getOrganization()));
		elementMap.put("Position", CmStringUtils.trimToEmpty(user.getPosition()));
		elementMap.put("Department", CmStringUtils.trimToEmpty(user.getDepartment()));
		elementMap.put("Language", CmStringUtils.trimToEmpty(user.getDisplayLanguage()));

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document getGeneralInfoForm(UserDto user)
	{
		Element loLastName = new Input("lastName", user.getLastName());
		loLastName.setLabel("Last Name");
		loLastName.addAttribute("REQUIRED");

		Element loFirstName = new Input("firstName", user.getFirstName());
		loFirstName.setLabel("First Name");

		Element loCompany = new Input("company", user.getCompany());
		loCompany.setLabel("Company");

		Element loOrganization = new Input("organization", user.getOrganization());
		loOrganization.setLabel("Organization");

		Element loPosition = new Input("position", user.getPosition());
		loPosition.setLabel("Position");

		Element loDepartment = new Input("department", user.getDepartment());
		loDepartment.setLabel("Department");

		Select loLanguage = new Select("language");
		loLanguage.setLabel("Language");
		loLanguage.setRequired(true);
		loLanguage.addAttribute("VALUE", user.getLanguage());
		loLanguage.addOption("en", new Locale("en").getDisplayLanguage());
		loLanguage.addOption("ko", new Locale("ko").getDisplayLanguage());

		Document document = new Document();
		document.addElement(loLastName);
		document.addElement(loFirstName);
		document.addElement(loCompany);
		document.addElement(loOrganization);
		document.addElement(loPosition);
		document.addElement(loDepartment);
		document.addElement(loLanguage);

		return document;
	}

	public static Document getAccountInfo(UserAccountDto userAccount)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Username", CmStringUtils.trimToEmpty(userAccount.getUsername()));
		elementMap.put("Account Locked", CmStringUtils.trimToEmpty(userAccount.getFgAccountLocked()));
		elementMap.put("Password Locked", CmStringUtils.trimToEmpty(userAccount.getFgPasswordLocked()));
		elementMap.put("No Use Password", CmStringUtils.trimToEmpty(userAccount.getFgNoUsePassword()));
		elementMap.put("Valid From", CmStringUtils.trimToEmpty(userAccount.getDisplayFromValid()));
		elementMap.put("Valid To", CmStringUtils.trimToEmpty(userAccount.getDisplayToValid()));

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document getAccountInfoForm(UserAccountDto userAccount)
	{
		InputPassword loPassword = new InputPassword("password");
		loPassword.setLabel("Password");
		loPassword.addAttribute("autofocus");

		InputPassword loConfirmPassword = new InputPassword("confirmPassword");
		loConfirmPassword.setLabel("Confirm Password");

		Checkbox loFgAccountLocked = new Checkbox("fgAccountLocked", PlatformCommonVo.YES, userAccount.getFgAccountLocked());
		loFgAccountLocked.setLabel("Account Locked");

		Element loFgPasswordLocked = new Checkbox("fgPasswordLocked", PlatformCommonVo.YES, userAccount.getFgPasswordLocked());
		loFgPasswordLocked.setLabel("Password Locked");

		Element loFgNoUsePassword = new Checkbox("fgNoUsePassword", PlatformCommonVo.YES, userAccount.getFgNoUsePassword());
		loFgNoUsePassword.setLabel("No Use Password");

		Input loFromValid = new Input("date", "fromValid", CmStringUtils.defaultString(userAccount.getDisplayFromValid(), CmDateFormatUtils.now()));
		loFromValid.setId("fromValid");
		loFromValid.setLabel("Valid From");

		Input loToValid = new Input("date", "toValid", CmStringUtils.defaultString(userAccount.getDisplayToValid(), CmDateFormatUtils.plusYears(10)));
		loToValid.setId("toValid");
		loToValid.setLabel("Valid To");

		Document document = new Document();

		if (CmStringUtils.isEmpty(userAccount.getUsername()))
		{
			loPassword.setRequired(true);
			loConfirmPassword.setRequired(true);

			Input loUsername = new Input("username");
			loUsername.setLabel("Username");
			loUsername.addAttribute("autofocus");
			loUsername.addAttribute("style", "text-transform:uppercase");
			loUsername.setRequired(true);

			document.addElement(loUsername);
		}

		document.addElement(loPassword);
		document.addElement(loConfirmPassword);
		document.addElement(loFgAccountLocked);
		document.addElement(loFgPasswordLocked);
		document.addElement(loFgNoUsePassword);
		document.addElement(loFromValid);
		document.addElement(loToValid);

		return document;
	}

	public static Document getContactInfo(UserContactInfoDto userContactInfo)
	{
		Map<String, Object> elementMap = new LinkedHashMap<>();
		elementMap.put("Email", CmStringUtils.trimToEmpty(userContactInfo.getEmail()));
		elementMap.put("Telephone", CmStringUtils.trimToEmpty(userContactInfo.getTel()));
		elementMap.put("Mobile", CmStringUtils.trimToEmpty(userContactInfo.getMobile()));
		elementMap.put("Fax", CmStringUtils.trimToEmpty(userContactInfo.getFax()));
		elementMap.put("Zipcode", CmStringUtils.trimToEmpty(userContactInfo.getZipcode()));
		elementMap.put("Street", CmStringUtils.trimToEmpty(userContactInfo.getStreet()));
		elementMap.put("City", CmStringUtils.trimToEmpty(userContactInfo.getCity()));
		elementMap.put("State", CmStringUtils.trimToEmpty(userContactInfo.getState()));
		elementMap.put("Country", CmStringUtils.trimToEmpty(userContactInfo.getDisplayCountry()));
		elementMap.put("Timezone", CmStringUtils.trimToEmpty(userContactInfo.getDisplayTimezone()));

		Document document = new Document();
		document.addElements(PlatformLayoutBuildUtils.simpleLabelTexts(elementMap));

		return document;
	}

	public static Document getContactInfoForm(UserContactInfoDto userContactInfo)
	{
		Input loEmail = new Input("email", CmStringUtils.trimToEmpty(userContactInfo.getEmail()));
		loEmail.setLabel("Email");
		loEmail.setRequired(true);

		Element loTelephone = new Element("group");
		loTelephone.addAttribute("type", "form");
		loTelephone.setLabel("Telephone");
		loTelephone.addElement(new Input("tel1", CmStringUtils.trimToEmpty(userContactInfo.getTel1())));
		loTelephone.addElement(new Input("tel2", CmStringUtils.trimToEmpty(userContactInfo.getTel2())));
		loTelephone.addElement(new Input("tel3", CmStringUtils.trimToEmpty(userContactInfo.getTel3())));

		Element loMobile = new Element("group");
		loMobile.addAttribute("type", "form");
		loMobile.setLabel("Mobile");
		loMobile.addElement(new Input("mobile1", CmStringUtils.trimToEmpty(userContactInfo.getMobile1())));
		loMobile.addElement(new Input("mobile2", CmStringUtils.trimToEmpty(userContactInfo.getMobile2())));
		loMobile.addElement(new Input("mobile3", CmStringUtils.trimToEmpty(userContactInfo.getMobile3())));

		Element loFax = new Element("group");
		loFax.addAttribute("type", "form");
		loFax.setLabel("Fax");
		loFax.addElement(new Input("fax1", CmStringUtils.trimToEmpty(userContactInfo.getFax1())));
		loFax.addElement(new Input("fax2", CmStringUtils.trimToEmpty(userContactInfo.getFax2())));
		loFax.addElement(new Input("fax3", CmStringUtils.trimToEmpty(userContactInfo.getFax3())));

		Input loZipcode = new Input("zipcode", CmStringUtils.trimToEmpty(userContactInfo.getZipcode()));
		loZipcode.setLabel("Zipcode");

		Input loStreet = new Input("street", CmStringUtils.trimToEmpty(userContactInfo.getStreet()));
		loStreet.setLabel("Street");

		Input loCity = new Input("city", CmStringUtils.trimToEmpty(userContactInfo.getCity()));
		loCity.setLabel("City");

		Input loState = new Input("state", CmStringUtils.trimToEmpty(userContactInfo.getState()));
		loState.setLabel("State");

		Select loCountry = new Select("country");
		loCountry.setLabel("Country");
		loCountry.setRequired(true);
		loCountry.setValue(CmStringUtils.trimToEmpty(userContactInfo.getCountry()));

		String[] locales = Locale.getISOCountries();

		for (String loCountryCode : locales)
		{
			Locale locale = new Locale("", loCountryCode);

			loCountry.addOption(locale.getCountry(), locale.getDisplayCountry());
		}

		Select loTimezone = new Select("timezone");
		loTimezone.setLabel("Timezone");
		loTimezone.setRequired(true);
		loTimezone.setValue(CmStringUtils.trimToEmpty(userContactInfo.getTimezone()));
		loTimezone.addAttribute("VALUE", CmStringUtils.trimToEmpty(userContactInfo.getTimezone()));

		String[] ids = TimeZone.getAvailableIDs(userContactInfo.getCountry());

		for (String id : ids)
		{
			TimeZone theTimezone = TimeZone.getTimeZone(id);

			loTimezone.addOption(id, id + ", " + theTimezone.getDisplayName());
		}

		Document document = new Document();
		document.addElement(loEmail);
		document.addElement(loTelephone);
		document.addElement(loMobile);
		document.addElement(loFax);
		document.addElement(loCity);
		document.addElement(loStreet);
		document.addElement(loState);
		document.addElement(loZipcode);
		document.addElement(loCountry);
		document.addElement(loTimezone);

		return document;
	}

	public static Document getAuthority(List<CItemDto> cItems)
	{
		Document document = new Document();

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItemDto : cItems)
			{
				document.addElement(PlatformLayoutBuildUtils.defaultLabelText(cItemDto.getName(), cItemDto.getDescription()));
			}
		}

		return document;
	}

	public static Document getAuthorityForm(List<CItemDto> cItems)
	{
		Tr thTr = new Tr();
		thTr.addCheckAllTh();
		thTr.addTh(new Th("Name"));
		thTr.addTh(new Th("Description"));

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		if (CollectionUtils.isNotEmpty(cItems))
		{
			for (CItemDto cItemDto : cItems)
			{
				Tr tbTr = new Tr();
				tbTr.addDataKeyCheckboxTd("cSeq", cItemDto.getCSeq());
				tbTr.addTextTd(cItemDto.getName());
				tbTr.addTextTd(cItemDto.getDescription());

				tbody.addTr(tbTr);
			}
		}

		Table table = new Table();
		table.addThead(thead);
		table.addTbody(tbody);

		Document document = new Document();
		document.addElement(table);

		return document;
	}

	public static Document getSimpleLoginHistories(List<LoginHistoryDto> loginHistories)
	{
		Tr thTr = new Tr();
		thTr.addTextTh("Login Date", "two wide");
		thTr.addTextTh("IP", "two wide");
		thTr.addTextTh("Referer", "five wide");
		thTr.addTextTh("User Agent", "six wide");

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		if (CollectionUtils.isNotEmpty(loginHistories))
		{
			for (LoginHistoryDto loginHistory : loginHistories)
			{
				Tr tbTr = new Tr();
				tbTr.addTextTd(CmDateFormatUtils.format(loginHistory.getRegDate()));
				tbTr.addTextTd(loginHistory.getIp());
				tbTr.addTextTd(loginHistory.getReferer());
				tbTr.addTextTd(loginHistory.getUserAgent());

				if (CmStringUtils.equals(loginHistory.getFgResult(), PlatformCommonVo.NO))
				{
					tbTr.setClass("error");
				}

				tbody.addTr(tbTr);
			}
		}

		Table table = new Table();
		table.addThead(thead);
		table.addTbody(tbody);

		Element loDivider = new Element("div");
		loDivider.addAttribute("class", "ui clearing divider");

//		Element loDescription = new Element("div");
//		loDescription.setText("This is a part of login history. If you want to check more data, please see them in Login History.");
//		loDescription.addElement(loDivider);

		Document document = new Document();
//		document.addElement(loDescription);
		document.addElement(table);

		return document;
	}

	public static Document getLoginHistories(PageableList<LoginHistoryDto> pageableList)
	{
		Th thLoginDate = new Th("Login Date");
		thLoginDate.addAttribute("CLASS", "two wide");

		Th thUsername = new Th("Username");
		thUsername.addAttribute("CLASS", "two wide");

		Th thIp = new Th("IP");
		thIp.addAttribute("CLASS", "two wide");

		Th thReferer = new Th("Referer");
		thReferer.addAttribute("CLASS", "five wide");

		Th thUserAgent = new Th("User Agent");
		thUserAgent.addAttribute("CLASS", "six wide");

		Tr thTr = new Tr();
		thTr.addTh(thLoginDate);
		thTr.addTh(thUsername);
		thTr.addTh(thIp);
		thTr.addTh(thReferer);
		thTr.addTh(thUserAgent);

		Thead thead = new Thead();
		thead.addTr(thTr);

		Tbody tbody = new Tbody();

		List<LoginHistoryDto> loginHistories = ListUtils.emptyIfNull(pageableList.getList());

		for (LoginHistoryDto loginHistory : loginHistories)
		{
			Tr tbTr = new Tr();
			tbTr.addTextTd(CmDateFormatUtils.format(loginHistory.getRegDate()));
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

		Table table = new Table();
		table.addThead(thead);
		table.addTbody(tbody);
		table.setPagination(pageableList);

		Document document = new Document();
		document.addElement(table);

		return document;
	}
}
