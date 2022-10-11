package org.jwebppy.portal.iv.mgmt.account.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmArrayUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemUserRlDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserContactInfoDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.service.UserGroupService;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.jwebppy.portal.iv.mgmt.account.dto.AccountDto;
import org.jwebppy.portal.iv.mgmt.account.dto.UserType;
import org.jwebppy.portal.iv.mgmt.account.utils.AccountMgmtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.util.TimeZone;

@Service
@Transactional
public class AccountMgmtService extends IvGeneralService
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private UserService userService;

	public DataList getMappingList(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SD_DEALER_MAPPING_LIST");

		rfcRequest
			.field().with(paramMap)
				.addByKey(new Object[][] {
					{"I_BNAME", "username"},
					{"I_KUNNR", "dealerCode"}
				})
			.and()
			.table().with(paramMap)
				.addByKey("T_VKORG", "salesAreaList");

		return simpleRfcTemplate.response(rfcRequest).getTable("T_LIST");
	}

	public RfcResponse getDealerGeneralInfo(String dealerCode)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ID_GEN");

		rfcRequest.addField("I_KUNNR", dealerCode);

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse getDealerInfo(Map<String, Object> paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SD_DEALER_LIST");

		rfcRequest
			.field().with(paramMap)
				.addByKey(new Object[][] {
					{"I_VTWEG", "distChl"},
					{"I_SPART", "division"},
					{"I_KUNNR", "dealerCode"}
			})
			.and()
			.table().with(paramMap)
				.addByKey("T_VKORG", "salesAreaList");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse saveMappingInfo(Map<String, Object> paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_MAPPING");

		rfcRequest
			.field().with(paramMap)
				.add("I_SELECT", "A")
				.addByKey(new Object[][] {
					{"I_BNAME", "username"},
					{"I_VKORG", "salesOrg"},
					{"I_VTWEG", "distChl"},
					{"I_SPART", "division"},
					{"I_KUNNR", "dealerCode"}
			});

		return simpleRfcTemplate.response(rfcRequest);
	}

	public boolean isValidDealer(AccountDto account)
	{
		String[] salesArea = AccountMgmtUtils.getSalesArea(account.getBizType());

		Map<String, Object> salesAreaMap = new HashMap<>();
		salesAreaMap.put("VKORG", salesArea[0]);

		List<Map<String, Object>> salesAreaList = new ArrayList<>();
		salesAreaList.add(salesAreaMap);

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("distChl", salesArea[1]);
		paramMap.put("division", salesArea[2]);
		paramMap.put("dealerCode", account.getDealerCode());
		paramMap.put("salesAreaList", salesAreaList);

		RfcResponse rfcResponse = getDealerInfo(paramMap);

		return CollectionUtils.isNotEmpty(rfcResponse.getTable("T_LIST"));
	}

	public int save(AccountDto account)
	{
		UserType userType = account.getUserType();

		//Dealer Code 유효성 체크
		if (!userType.equals(UserType.I))
		{
			if (!isValidDealer(account))
			{
				return -1;
			}
		}

		//아이디는 대문자로만 생성.
		String username = getUsername(account).toUpperCase();

		if (CmStringUtils.isEmpty(username))
		{
			return -4;
		}

		account.setUsername(username);

		if (userService.isExistByUsername(username))
		{
			return -2;
		}

		String bizType = account.getBizType();

		//내부사용자는 임의의 Dealer Code 를 지정함.
		if (userType.equals(UserType.I))
		{
			account.setDealerCode(AccountMgmtUtils.getDefaultMappingCode(bizType, userType));
		}

		String dealerCode = account.getDealerCode();
		RfcResponse rfcRespose = getDealerGeneralInfo(dealerCode);
		DataMap userInfoMap = (DataMap)rfcRespose.getTable("O_KNA").get(0);

		String tzone = CmStringUtils.removeEnd(rfcRespose.getString("O_TZONE"), "00");

		if (CmStringUtils.equals(tzone, "P00"))
		{
			tzone = "PGM";
		}

		UserGroupDto userGroup = userGroupService.getUserGroupByName(AccountMgmtUtils.getUserGroupName(bizType, tzone));

		String dealerName = userInfoMap.getString("NAME1");

		//사용자 기본정보
		UserDto user = new UserDto();
		user.setLastName(CmStringUtils.defaultIfEmpty(account.getLastName(), dealerName));
		user.setFirstName(account.getFirstName());
		user.setCompany(dealerName);
		user.setLanguage(userGroup.getDefLang());
		user.setUserGroup(userGroup);

		int uSeq = userService.saveUser(user);

		//사용자 계정
		UserAccountDto userAccount = new UserAccountDto();
		userAccount.setUSeq(uSeq);
		userAccount.setUsername(username);
		userAccount.setFgAccountLocked(PlatformCommonVo.NO);
		userAccount.setCredentialsPolicy(userGroup.getCredentialsPolicy());
		userAccount.setPassword(IvCommonVo.INITIAL_PASSWORD);
		userAccount.setFgNoUsePassword((UserType.I.equals(userType)) ? IvCommonVo.YES : IvCommonVo.NO);//내부사용자는 AD 를 사용하기 때문에 비밀번호 설정이 필요하지 않음
		userAccount.setFgPasswordLocked((UserType.I.equals(userType)) ? IvCommonVo.NO : IvCommonVo.YES);

		userService.saveUserAccount(userAccount);

		//사용자 contact
		String country = IvCommonVo.DEFAULT_COUNTRY;
		String timezone = IvCommonVo.DEFAULT_TIMEZONE;
		String[] tzoneIds = TimeZone.getAvailableIDs(userInfoMap.getString("LAND1"));

		if (ArrayUtils.isNotEmpty(tzoneIds))
		{
			country = userInfoMap.getString("LAND1");
			timezone = tzoneIds[0];
		}

		UserContactInfoDto userContactInfo = new UserContactInfoDto();
		userContactInfo.setUSeq(uSeq);
		userContactInfo.setCountry(country);
		userContactInfo.setTimezone(timezone);

		userService.saveUserContactInfo(userContactInfo);

		//사용자 권한
		String[] authorities = AccountMgmtUtils.getAuthorities(bizType, userType);

		if (CmArrayUtils.isNotEmpty(authorities))
		{
			CItemUserRlDto cItemUserRl = new CItemUserRlDto();
			cItemUserRl.setUSeq(uSeq);

			for (String authority : authorities)
			{
				cItemUserRl.setName(authority);

				contentAuthorityService.saveByCItemName(cItemUserRl);
			}
		}

		String[] salesArea = AccountMgmtUtils.getSalesArea(bizType);

		if (CmArrayUtils.isNotEmpty(salesArea))
		{
			ErpDataMap paramMap = new ErpDataMap();

			paramMap.add(new String[][] {
				{"salesOrg", salesArea[0]},
				{"distChl", salesArea[1]},
				{"division", salesArea[2]},
				{"dealerCode", CmStringUtils.leftPad(dealerCode, 10, "0")},
				{"username", username}
			});

			saveMappingInfo(paramMap);
		}

		return uSeq;
	}

	protected String getUsername(AccountDto account)
	{
		String username = CmStringUtils.trimToEmpty(account.getUsername());

		if (!UserType.I.equals(account.getUserType()))
		{
			String bizType = account.getBizType();

			if (CmStringUtils.equalsAny(bizType, "HQDOP", "HQEXP"))
			{
				RfcResponse dealerInfo = getDealerGeneralInfo(account.getDealerCode());
				DataList dealerInfoList = dealerInfo.getTable("O_KNA");

				if (CollectionUtils.isNotEmpty(dealerInfoList))
				{
					DataMap dealerDetailMap = (DataMap)dealerInfoList.get(0);

					/* DoobizPlus 아이디 발번 규칙
					 * 유럽/영국법인: 계정 생성 시 직접입력
					 * 본사 내수/수출: 'D' + 국가코드2자리 + 모듈코드(P:부품,S:완성차,C:서비스)
					 */
					String prefix = "D" + dealerDetailMap.getString("LAND1") + dealerInfo.getString("PV_CD");

					return generateUsername(bizType, prefix);
				}
			}
			else if (CmStringUtils.equalsAny(bizType, "EUDOP", "UKDOP"))
			{
				//유럽, 영국법인 계정은 발번 규칙을 따르지 않음. AD 와의 중복을 파하기 위해 딜러 아이디에 prefix 추가
				if (!CmStringUtils.startsWith(username, EuCommonVo.EUUK_USERNAME_PREFIX))
				{
					return EuCommonVo.EUUK_USERNAME_PREFIX + username;
				}
			}
		}

		return username;
	}

	protected String generateUsername(String bizType, String prefix)
	{
		String[] salesArea = AccountMgmtUtils.getSalesArea(bizType);

		Map<String, Object> salesAreaMap = new HashMap<>();
		salesAreaMap.put("VKORG", salesArea[0]);
		salesAreaMap.put("VTWEG", salesArea[1]);

		List<Map<String, Object>> salesAreaList = new ArrayList<>();
		salesAreaList.add(salesAreaMap);

		ErpDataMap paramMap = new ErpDataMap();
		paramMap.add("salesAreaList", salesAreaList);

		DataList dataList = getMappingList(paramMap);
		String username = prefix + "0000";

		for (int i=0, size=dataList.size(); i<size; i++)
		{
			DataMap dataMap = (DataMap)dataList.get(i);

			String bname = dataMap.getString("BNAME");

			if (CmStringUtils.startsWithIgnoreCase(bname, prefix))
			{
				if (bname.compareToIgnoreCase(username) > 0)
				{
					username = bname;
				}
			}
		}

		int seq = 0;

		try
		{
			seq = Integer.parseInt(CmStringUtils.remove(username, prefix)) + 1;
		}
		catch (NumberFormatException e)
		{
			seq = 1;
		}

		return checkDuplicateUsername(prefix, seq);
	}

	private String checkDuplicateUsername(String prefix, int seq)
	{
		String username = prefix + CmStringUtils.leftPad(seq, 4, "0");

		if (!userService.isExistByUsername(username))
		{
			return username;
		}

		return checkDuplicateUsername(prefix, ++seq);
	}
}
