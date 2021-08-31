package org.jwebppy.platform.mgmt.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicySearchDto;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyType;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyVo;
import org.jwebppy.platform.mgmt.user.entity.CredentialsPolicyEntity;
import org.jwebppy.platform.mgmt.user.mapper.CredentialsPolicyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CredentialsPolicyService extends GeneralService
{
	@Autowired
	private CredentialsPolicyMapper credentialsPolicyMapper;

	public int save(CredentialsPolicyDto credentialsPolicy)
	{
		return credentialsPolicyMapper.insert(CmModelMapperUtils.map(credentialsPolicy, CredentialsPolicyEntity.class));
	}

	public int modify(CredentialsPolicyDto credentialsPolicy)
	{
		return credentialsPolicyMapper.update(CmModelMapperUtils.map(credentialsPolicy, CredentialsPolicyEntity.class));
	}

	public int delete(Integer cpSeq)
	{
		return credentialsPolicyMapper.updateFgDelete(cpSeq);
	}

	public CredentialsPolicyDto getCredentialPolicy(Integer cpSeq)
	{
		return CmModelMapperUtils.map(credentialsPolicyMapper.findCredentialsPolicy(cpSeq), CredentialsPolicyDto.class);
	}

	public CredentialsPolicyDto getDefaultCredentialPolicy(CredentialsPolicySearchDto credentialsPolicySearch)
	{
		return CmModelMapperUtils.map(credentialsPolicyMapper.findDefaultCredentialsPolicy(credentialsPolicySearch), CredentialsPolicyDto.class);
	}

	public CredentialsPolicyDto getDefaultCredentialPolicyIfEmpty(CredentialsPolicySearchDto credentialsPolicySearch)
	{
		Integer cpSeq = credentialsPolicySearch.getCpSeq();
		CredentialsPolicyEntity credentialsPolicy = null;

		if (cpSeq == null)
		{
			credentialsPolicy = credentialsPolicyMapper.findDefaultCredentialsPolicy(credentialsPolicySearch);
		}
		else
		{
			credentialsPolicy = credentialsPolicyMapper.findCredentialsPolicy(cpSeq);
		}

		return CmModelMapperUtils.map(credentialsPolicy, CredentialsPolicyDto.class);
	}

	public List<CredentialsPolicyDto> getCredentialPolicies(CredentialsPolicySearchDto credentialsPolicySearch)
	{
		return CmModelMapperUtils.mapAll(credentialsPolicyMapper.findCredentialsPolicies(credentialsPolicySearch), CredentialsPolicyDto.class);
	}

	public List<CredentialsPolicyDto> getPageableCredentialPolicies(CredentialsPolicySearchDto credentialsPolicySearch)
	{
		return CmModelMapperUtils.mapAll(credentialsPolicyMapper.findPageCredentialsPolicies(credentialsPolicySearch), CredentialsPolicyDto.class);
	}

	public Map<String, Object> checkValid(CredentialsPolicyDto credentialsPolicy, CredentialsPolicyType type ,String str)
	{
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("TYPE", type);

		if (credentialsPolicy == null || CmStringUtils.isEmpty(str))
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
		int iMaxLength = CmNumberUtils.toInt(maxLength, PlatformCommonVo.CREDENTIAL_MAX_LENGTH);
		int iMinLowerCase = CmNumberUtils.toInt(minLowercase);
		int iMaxLowerCase = CmNumberUtils.toInt(maxLowercase, PlatformCommonVo.CREDENTIAL_MAX_LENGTH);
		int iMinUpperCase = CmNumberUtils.toInt(minUppercase);
		int iMaxUpperCase = CmNumberUtils.toInt(maxUppercase, PlatformCommonVo.CREDENTIAL_MAX_LENGTH);
		int iMinNumber = CmNumberUtils.toInt(minNumber);
		int iMaxNumber = CmNumberUtils.toInt(maxNumber, PlatformCommonVo.CREDENTIAL_MAX_LENGTH);
		int iMinSpecial = CmNumberUtils.toInt(minSpecial);
		int iMaxSpecial = CmNumberUtils.toInt(maxSpecial, PlatformCommonVo.CREDENTIAL_MAX_LENGTH);

		int numberCount = getMatchedCount(PlatformCommonVo.CREDENTIAL_NUMBER, str);
		int lowercaseCount = getMatchedCount(PlatformCommonVo.CREDENTIAL_LOWERCASE, str);
		int uppercaseCount = getMatchedCount(PlatformCommonVo.CREDENTIAL_UPPERCASE, str);
		int specialCount = getMatchedCount(PlatformCommonVo.CREDENTIAL_SPECIAL, str);

		int strLength = str.length();
		boolean isValid = false;

		if (strLength < iMinLength || strLength > iMaxLength)
		{
			resultMap.put("RESULT", CredentialsPolicyVo.WRONG_LENGTH);
			resultMap.put("MIN", minLength);
			resultMap.put("MAX", maxLength);
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

		messages.add("Length of " + target + " should be between " + minLength + " and " + maxLength + ".");

		if (CmStringUtils.isNotEmpty(minLowercase) && CmStringUtils.isNotEmpty(maxLowercase))
		{
			messages.add(target + " should include lowercase letters between " + minLowercase + " and " + maxLowercase + ".");
		}
		else if (CmStringUtils.isNotEmpty(minLowercase) && CmStringUtils.isEmpty(maxLowercase))
		{
			messages.add(target + " should include a minimum of " + minLowercase + " lowercase letters.");
		}
		else if (CmStringUtils.isEmpty(minLowercase) && CmStringUtils.isNotEmpty(maxLowercase))
		{
			messages.add(target + " should include a maximum of " + maxLowercase + " lowercase letters.");
		}

		if (CmStringUtils.isNotEmpty(minUppercase) && CmStringUtils.isNotEmpty(maxUppercase))
		{
			messages.add(target + " should include uppercase letters between " + minUppercase + " and " + maxUppercase + ".");
		}
		else if (CmStringUtils.isNotEmpty(minUppercase) && CmStringUtils.isEmpty(maxUppercase))
		{
			messages.add(target + " should include a minimum of " + minUppercase + " uppercase letters.");
		}
		else if (CmStringUtils.isEmpty(minUppercase) && CmStringUtils.isNotEmpty(maxUppercase))
		{
			messages.add(target + " should include a maximum of " + maxUppercase + " uppercase letters.");
		}

		if (CmStringUtils.isNotEmpty(minNumber) && CmStringUtils.isNotEmpty(maxNumber))
		{
			messages.add(target + " should include numbers between " + minNumber + " and " + maxNumber + " digits.");
		}
		else if (CmStringUtils.isNotEmpty(minNumber) && CmStringUtils.isEmpty(maxNumber))
		{
			messages.add(target + " should include a minimum of " + minNumber + " digits.");
		}
		else if (CmStringUtils.isEmpty(minNumber) && CmStringUtils.isNotEmpty(maxNumber))
		{
			messages.add(target + " should include a maximum of " + maxNumber + " digits.");
		}

		if (CmStringUtils.isNotEmpty(minSpecial) && CmStringUtils.isNotEmpty(maxSpecial))
		{
			messages.add(target + " should include special letters between " + minSpecial + " and " + maxSpecial + ".");
		}
		else if (CmStringUtils.isNotEmpty(minSpecial) && CmStringUtils.isEmpty(maxSpecial))
		{
			messages.add(target + " should include a minimum of " + minSpecial + " special letters.");
		}
		else if (CmStringUtils.isEmpty(minSpecial) && CmStringUtils.isNotEmpty(maxSpecial))
		{
			messages.add(target + " should include a maximum of " + maxSpecial + " special letters.");
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
