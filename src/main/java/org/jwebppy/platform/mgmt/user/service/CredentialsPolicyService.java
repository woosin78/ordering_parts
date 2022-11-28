package org.jwebppy.platform.mgmt.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicySearchDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyType;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyVo;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.mapper.CredentialsPolicyMapper;
import org.jwebppy.platform.mgmt.user.mapper.CredentialsPolicyObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CredentialsPolicyService extends GeneralService
{
	@Autowired
	private CredentialsPolicyMapper credentialsPolicyMapper;

	@Autowired
	private I18nMessageSource i18nMessageSource;

	@Autowired
	private UserService userService;

	public int create(CredentialsPolicyDto credentialsPolicy)
	{
		return credentialsPolicyMapper.insert(CmModelMapperUtils.mapToEntity(CredentialsPolicyObjectMapper.INSTANCE, credentialsPolicy));
	}

	public int modify(CredentialsPolicyDto credentialsPolicy)
	{
		return credentialsPolicyMapper.update(CmModelMapperUtils.mapToEntity(CredentialsPolicyObjectMapper.INSTANCE, credentialsPolicy));
	}

	public int delete(Integer cpSeq)
	{
		return credentialsPolicyMapper.updateFgDelete(cpSeq);
	}

	public int delete(List<Integer> cpSeqs)
	{
		int result = 0;

		if (CollectionUtils.isNotEmpty(cpSeqs))
		{
			for (Integer cpSeq: cpSeqs)
			{
				result += delete(cpSeq);
			}
		}

		return result;
	}

	public int save(CredentialsPolicyDto credentialsPolicy)
	{
		if (credentialsPolicy.getCpSeq() == null)
		{
			return create(credentialsPolicy);
		}
		else
		{
			return modify(credentialsPolicy);
		}
	}

	public CredentialsPolicyDto getCredentialPolicy(Integer cpSeq)
	{
		return CmModelMapperUtils.mapToDto(CredentialsPolicyObjectMapper.INSTANCE, credentialsPolicyMapper.findCredentialsPolicy(cpSeq));
	}

	public CredentialsPolicyDto getCredentialPolicyByName(String name)
	{
		return CmModelMapperUtils.mapToDto(CredentialsPolicyObjectMapper.INSTANCE, credentialsPolicyMapper.findCredentialsPolicyByName(name));
	}

	public CredentialsPolicyDto getDefaultCredentialPolicyIfEmpty(CredentialsPolicySearchDto credentialsPolicySearch)
	{
		Integer cpSeq = credentialsPolicySearch.getCpSeq();
		Integer uSeq = credentialsPolicySearch.getUSeq();

		if (ObjectUtils.isEmpty(cpSeq))
		{
			if (ObjectUtils.isNotEmpty(uSeq))
			{
				UserDto user = userService.getUser(uSeq);

				cpSeq = user.getUserGroup().getCredentialsPolicy().getCpSeq();
			}
		}

		//Get default credentials policy
		if (ObjectUtils.isEmpty(cpSeq))
		{
			CredentialsPolicySearchDto credentialsPolicySearch2 = new CredentialsPolicySearchDto();
			credentialsPolicySearch2.setFgUse(MgmtCommonVo.YES);
			credentialsPolicySearch2.setFgDefault(MgmtCommonVo.YES);

			List<CredentialsPolicyDto> credentialsPolicies = getCredentialsPolicies(credentialsPolicySearch2);

			if (CollectionUtils.isNotEmpty(credentialsPolicies))
			{
				return credentialsPolicies.get(0);
			}
		}
		else
		{
			return getCredentialPolicy(cpSeq);
		}

		return null;
	}

	public List<CredentialsPolicyDto> getCredentialsPolicies(CredentialsPolicySearchDto credentialsPolicySearch)
	{
		return CmModelMapperUtils.mapToDto(CredentialsPolicyObjectMapper.INSTANCE, credentialsPolicyMapper.findCredentialsPolicies(credentialsPolicySearch));
	}

	public List<CredentialsPolicyDto> getPageableCredentialsPolicies(CredentialsPolicySearchDto credentialsPolicySearch)
	{
		return CmModelMapperUtils.mapToDto(CredentialsPolicyObjectMapper.INSTANCE, credentialsPolicyMapper.findPageCredentialsPolicies(credentialsPolicySearch));
	}

	public Map<String, Object> checkValid(CredentialsPolicyDto credentialsPolicy, CredentialsPolicyType type ,String str)
	{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("TYPE", type);

		if (credentialsPolicy == null)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.VALID);

			return resultMap;
		}

		if (CmStringUtils.isEmpty(str))
		{
			resultMap.put("RESULT", CredentialsPolicyVo.EMPTY);

			return resultMap;
		}

		String minLength;
		String maxLength;
		String minLowercase;
		String maxLowercase;
		String minUppercase;
		String maxUppercase;
		String minNumber;
		String maxNumber;
		String minSpecial;
		String maxSpecial;
		String fgOnlyLowercase = MgmtCommonVo.NO;
		String fgOnlyUppercase = MgmtCommonVo.NO;

		if (type == CredentialsPolicyType.U)
		{
			minLength = credentialsPolicy.getUMinLength();
			maxLength = credentialsPolicy.getUMaxLength();
			minLowercase = credentialsPolicy.getUMinLowercase();
			maxLowercase = credentialsPolicy.getUMaxLowercase();
			minUppercase = credentialsPolicy.getUMinUppercase();
			maxUppercase = credentialsPolicy.getUMaxUppercase();
			minNumber = credentialsPolicy.getUMinNumber();
			maxNumber = credentialsPolicy.getUMaxNumber();
			minSpecial = credentialsPolicy.getUMinSpecial();
			maxSpecial = credentialsPolicy.getUMaxSpecial();
			fgOnlyLowercase = credentialsPolicy.getUFgOnlyLowercase();
			fgOnlyUppercase = credentialsPolicy.getUFgOnlyUppercase();
		}
		else
		{
			minLength = credentialsPolicy.getPMinLength();
			maxLength = credentialsPolicy.getPMaxLength();
			minLowercase = credentialsPolicy.getPMinLowercase();
			maxLowercase = credentialsPolicy.getPMaxLowercase();
			minUppercase = credentialsPolicy.getPMinUppercase();
			maxUppercase = credentialsPolicy.getPMaxUppercase();
			minNumber = credentialsPolicy.getPMinNumber();
			maxNumber = credentialsPolicy.getPMaxNumber();
			minSpecial = credentialsPolicy.getPMinSpecial();
			maxSpecial = credentialsPolicy.getPMaxSpecial();
		}

		int iMinLength = CmNumberUtils.toInt(minLength);
		int iMaxLength = CmNumberUtils.toInt(maxLength, PlatformConfigVo.CREDENTIAL_MAX_LENGTH);
		int iMinLowerCase = CmNumberUtils.toInt(minLowercase);
		int iMaxLowerCase = CmNumberUtils.toInt(maxLowercase, PlatformConfigVo.CREDENTIAL_MAX_LENGTH);
		int iMinUpperCase = CmNumberUtils.toInt(minUppercase);
		int iMaxUpperCase = CmNumberUtils.toInt(maxUppercase, PlatformConfigVo.CREDENTIAL_MAX_LENGTH);
		int iMinNumber = CmNumberUtils.toInt(minNumber);
		int iMaxNumber = CmNumberUtils.toInt(maxNumber, PlatformConfigVo.CREDENTIAL_MAX_LENGTH);
		int iMinSpecial = CmNumberUtils.toInt(minSpecial);
		int iMaxSpecial = CmNumberUtils.toInt(maxSpecial, PlatformConfigVo.CREDENTIAL_MAX_LENGTH);

		int numberCount = getMatchedCount(PlatformConfigVo.CREDENTIAL_NUMBER, str);
		int lowercaseCount = getMatchedCount(PlatformConfigVo.CREDENTIAL_LOWERCASE, str);
		int uppercaseCount = getMatchedCount(PlatformConfigVo.CREDENTIAL_UPPERCASE, str);
		int specialCount = getMatchedCount(PlatformConfigVo.CREDENTIAL_SPECIAL, str);

		int strLength = str.length();
		boolean isValid = false;

		if (strLength < iMinLength || strLength > iMaxLength)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.WRONG_LENGTH);
			resultMap.put("MIN", minLength);
			resultMap.put("MAX", maxLength);
		}
		else if (CmStringUtils.equals(fgOnlyLowercase, MgmtCommonVo.YES) && uppercaseCount > 0)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.WRONG_ONLY_LOWERCASE);
		}
		else if (CmStringUtils.equals(fgOnlyUppercase, MgmtCommonVo.YES) && lowercaseCount > 0)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.WRONG_ONLY_UPPERCASE);
		}
		else if (lowercaseCount < iMinLowerCase || lowercaseCount > iMaxLowerCase)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.WRONG_LOWERCASE_COUNT);
			resultMap.put("MIN", minLowercase);
			resultMap.put("MAX", maxLowercase);
		}
		else if (uppercaseCount < iMinUpperCase || uppercaseCount > iMaxUpperCase)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.WRONG_UPPERCASE_COUNT);
			resultMap.put("MIN", minUppercase);
			resultMap.put("MAX", maxUppercase);
		}
		else if (numberCount < iMinNumber || numberCount > iMaxNumber)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.WRONG_NUMBER_COUNT);
			resultMap.put("MIN", minNumber);
			resultMap.put("MAX", maxNumber);
		}
		else if (specialCount < iMinSpecial || specialCount > iMaxSpecial)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.WRONG_SPECIAL_COUNT);
			resultMap.put("MIN", minSpecial);
			resultMap.put("MAX", maxSpecial);
		}
		else
		{
			resultMap.put("RESULT", CredentialsPolicyVo.VALID);
			isValid = true;
		}

		resultMap.put("MESSAGE", (isValid) ? Collections.EMPTY_LIST : getMessage(credentialsPolicy, type));

		return resultMap;
	}

	public List<String> getMessage(CredentialsPolicyDto credentialsPolicy, CredentialsPolicyType type)
	{
		String minLength;
		String maxLength;
		String minLowercase;
		String maxLowercase;
		String minUppercase;
		String maxUppercase;
		String minNumber;
		String maxNumber;
		String minSpecial;
		String maxSpecial;
		String fgOnlyUppercase = MgmtCommonVo.NO;
		String fgOnlyLowercase = MgmtCommonVo.NO;
		String target = type.getType();

		if (type == CredentialsPolicyType.U)
		{
			minLength = credentialsPolicy.getUMinLength();
			maxLength = credentialsPolicy.getUMaxLength();
			minLowercase = credentialsPolicy.getUMinLowercase();
			maxLowercase = credentialsPolicy.getUMaxLowercase();
			minUppercase = credentialsPolicy.getUMinUppercase();
			maxUppercase = credentialsPolicy.getUMaxUppercase();
			minNumber = credentialsPolicy.getUMinNumber();
			maxNumber = credentialsPolicy.getUMaxNumber();
			minSpecial = credentialsPolicy.getUMinSpecial();
			maxSpecial = credentialsPolicy.getUMaxSpecial();
			fgOnlyLowercase = credentialsPolicy.getUFgOnlyLowercase();
			fgOnlyUppercase = credentialsPolicy.getUFgOnlyUppercase();
		}
		else
		{
			minLength = credentialsPolicy.getPMinLength();
			maxLength = credentialsPolicy.getPMaxLength();
			minLowercase = credentialsPolicy.getPMinLowercase();
			maxLowercase = credentialsPolicy.getPMaxLowercase();
			minUppercase = credentialsPolicy.getPMinUppercase();
			maxUppercase = credentialsPolicy.getPMaxUppercase();
			minNumber = credentialsPolicy.getPMinNumber();
			maxNumber = credentialsPolicy.getPMaxNumber();
			minSpecial = credentialsPolicy.getPMinSpecial();
			maxSpecial = credentialsPolicy.getPMaxSpecial();
		}

		List<String> messages = new ArrayList<>();

		//messages.add("Length of " + target + " should be between " + minLength + " and " + maxLength + ".");
		messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_11", new String[] {target, minLength, maxLength}));

		if (CmStringUtils.isNotEmpty(minLowercase) && CmStringUtils.isNotEmpty(maxLowercase))
		{
			//messages.add(target + " should include lower case letters between " + minLowercase + " and " + maxLowercase + ".");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_21", new String[] {target, minLowercase, maxLowercase}));
		}
		else if (CmStringUtils.isNotEmpty(minLowercase) && CmStringUtils.isEmpty(maxLowercase))
		{
			//messages.add(target + " should include a minimum of " + minLowercase + " lower case letter(s).");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_22", new String[] {target, minLowercase}));
		}
		else if (CmStringUtils.isEmpty(minLowercase) && CmStringUtils.isNotEmpty(maxLowercase))
		{
			//messages.add(target + " should include a maximum of " + maxLowercase + " lower case letter(s).");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_23", new String[] {target, maxLowercase}));
		}

		if (CmStringUtils.isNotEmpty(minUppercase) && CmStringUtils.isNotEmpty(maxUppercase))
		{
			//messages.add(target + " should include upper case letters between " + minUppercase + " and " + maxUppercase + ".");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_31", new String[] {target, minUppercase, maxUppercase}));
		}
		else if (CmStringUtils.isNotEmpty(minUppercase) && CmStringUtils.isEmpty(maxUppercase))
		{
			//messages.add(target + " should include a minimum of " + minUppercase + " upper case letter(s).");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_32", new String[] {target, minUppercase}));
		}
		else if (CmStringUtils.isEmpty(minUppercase) && CmStringUtils.isNotEmpty(maxUppercase))
		{
			//messages.add(target + " should include a maximum of " + maxUppercase + " upper case letter(s).");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_33", new String[] {target, maxUppercase}));
		}

		if (CmStringUtils.isNotEmpty(minNumber) && CmStringUtils.isNotEmpty(maxNumber))
		{
			//messages.add(target + " should include numbers between " + minNumber + " and " + maxNumber + " digits.");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_41", new String[] {target, minNumber, maxNumber}));
		}
		else if (CmStringUtils.isNotEmpty(minNumber) && CmStringUtils.isEmpty(maxNumber))
		{
			//messages.add(target + " should include a minimum of " + minNumber + " digit(s).");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_42", new String[] {target, minNumber}));
		}
		else if (CmStringUtils.isEmpty(minNumber) && CmStringUtils.isNotEmpty(maxNumber))
		{
			//messages.add(target + " should include a maximum of " + maxNumber + " digit(s).");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_43", new String[] {target, maxNumber}));
		}

		if (CmStringUtils.isNotEmpty(minSpecial) && CmStringUtils.isNotEmpty(maxSpecial))
		{
			//messages.add(target + " should include special letters between " + minSpecial + " and " + maxSpecial + ".");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_51", new String[] {target, minSpecial, maxSpecial}));
		}
		else if (CmStringUtils.isNotEmpty(minSpecial) && CmStringUtils.isEmpty(maxSpecial))
		{
			//messages.add(target + " should include a minimum of " + minSpecial + " special letter(s).");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_52", new String[] {target, minSpecial}));
		}
		else if (CmStringUtils.isEmpty(minSpecial) && CmStringUtils.isNotEmpty(maxSpecial))
		{
			//messages.add(target + " should include a maximum of " + maxSpecial + " special letter(s).");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_53", new String[] {target, maxSpecial}));
		}

		if (CmStringUtils.equals(fgOnlyLowercase, MgmtCommonVo.YES))
		{
			//messages.add(target + " should be only lower case.");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_61", new String[] {target}));
		}

		if (CmStringUtils.equals(fgOnlyUppercase, MgmtCommonVo.YES))
		{
			//messages.add(target + " should be only upper case.");
			messages.add(i18nMessageSource.getMessage("PLTF_M_VALID_CREDENTIALS_RULE_62", new String[] {target}));
		}

		return messages;
	}

	private int getMatchedCount(String[] targets , String str)
	{
		if (targets == null || str == null || str.length() == 0)
		{
			return 0;
		}

		int count = 0;

		for (String ch: targets)
		{
			for (int i=0, length=str.length(); i<length; i++)
			{
				if (ch.equals(str.substring(i, i+1)))
				{
					count++;
				}
			}
		}

		return count;
	}
}
