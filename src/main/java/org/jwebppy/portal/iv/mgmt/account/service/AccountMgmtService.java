package org.jwebppy.portal.iv.mgmt.account.service;

import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
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
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.dto.UserGroupDto;
import org.jwebppy.platform.mgmt.user.service.UserGroupService;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.jwebppy.portal.iv.mgmt.account.utils.AccountMgmtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public RfcResponse getDealerInfoByDealerCode(String dealerCode)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_ID_GEN");

		rfcRequest.addField("I_KUNNR", dealerCode);
		rfcRequest.addOutputParameter("O_KNA");

		return simpleRfcTemplate.response(rfcRequest);
	}

	public RfcResponse saveMappingInfo(Map<String, Object> rfcParamMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_EP_GET_MAPPING");

		rfcRequest
			.field().with(rfcParamMap)
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

	public int save(ErpDataMap rfcParamMap)
	{
		//아이디는 대문자로만 생성.
		String username = rfcParamMap.getString("username").toUpperCase();

		if (CmStringUtils.isEmpty(username))
		{
			return -4;
		}

		rfcParamMap.put("username", username);

		String bizType = rfcParamMap.getString("bizType");
		String userType = rfcParamMap.getString("userType");

		//AD 와 중복을 파하기 위해 딜러 계정은 prefix 추가
		if ("D".equals(userType))
		{
			if (!CmStringUtils.startsWith(username, IvCommonVo.EUUK_USERNAME_PREFIX))
			{
				username = IvCommonVo.EUUK_USERNAME_PREFIX + username;
			}

			rfcParamMap.put("username", username);
		}

		if (userService.isExistByUsername(username))
		{
			return -2;
		}

		//내부사용자는 임의의 Dealer Code 를 지정함.
		if ("I".equals(userType))
		{
			rfcParamMap.put("dealerCode", AccountMgmtUtils.getDefaultMappingCode(bizType, userType));
		}

		String dealerCode = rfcParamMap.getString("dealerCode");
		DataList dealerInfoList = getDealerInfoByDealerCode(dealerCode).getTable("O_KNA");

		if (CollectionUtils.isEmpty(dealerInfoList))
		{
			return -1;
		}

		UserGroupDto userGroup = userGroupService.getUserGroupByName(AccountMgmtUtils.getUserGroupName(bizType));

		String dealerName = ((DataMap)dealerInfoList.get(0)).getString("NAME1");

		UserDto user = new UserDto();
		user.setLastName(CmStringUtils.defaultIfEmpty(rfcParamMap.getString("lastName"), dealerName));
		user.setFirstName(rfcParamMap.getString("firstName"));
		user.setCompany(dealerName);
		user.setLanguage(userGroup.getDefLang());
		user.setUserGroup(userGroup);

		int uSeq = userService.saveUser(user);

		UserAccountDto userAccount = new UserAccountDto();
		userAccount.setUSeq(uSeq);
		userAccount.setUsername(username);
		userAccount.setFgAccountLocked(PlatformCommonVo.NO);
		userAccount.setCredentialsPolicy(userGroup.getCredentialsPolicy());

		//내부사용자는 AD 를 사용하기 때문에 비밀번호 설정이 필요하지 않음
		if ("I".equals(userType))
		{
			userAccount.setPassword(IvCommonVo.INITIAL_PASSWORD);
			userAccount.setFgNoUsePassword(IvCommonVo.YES);
			userAccount.setFgPasswordLocked(IvCommonVo.NO);
		}
		else
		{
			userAccount.setPassword(IvCommonVo.INITIAL_PASSWORD);
			userAccount.setFgPasswordLocked(IvCommonVo.YES);
		}

		userService.saveUserAccount(userAccount);

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
			rfcParamMap.add(new String[][] {
				{"salesOrg", salesArea[0]},
				{"distChl", salesArea[1]},
				{"division", salesArea[2]},
				{"dealerCode", CmStringUtils.leftPad(dealerCode, 10, "0")},
			});

			saveMappingInfo(rfcParamMap);
		}

		return uSeq;
	}
}
