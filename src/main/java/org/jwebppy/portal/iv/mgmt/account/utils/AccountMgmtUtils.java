package org.jwebppy.portal.iv.mgmt.account.utils;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.portal.iv.mgmt.account.dto.UserType;

public class AccountMgmtUtils
{
	private final static Map<String, String> BIZ_TYPE = new HashMap<>();
	private final static Map<String, String[]> SALES_AREA = new HashMap<>();
	private final static Map<String, String> MAPPING_CODE = new HashMap<>();
	private final static Map<String, String> USER_GROUP = new HashMap<>();
	private final static Map<String, String[]> AUTHORITY = new HashMap<>();

	static {
		BIZ_TYPE.put("781610", "EUDOP");
		BIZ_TYPE.put("721610", "UKDOP");
		BIZ_TYPE.put("F11610", "HQDOP");
		BIZ_TYPE.put("F11620", "HQEXP");

		SALES_AREA.put("EUDOP", new String[] {"7816", "10", "60"});//EUDOP
		SALES_AREA.put("UKDOP", new String[] {"7216", "10", "60"});//UKDOP
		SALES_AREA.put("HQDOP", new String[] {"F116", "10", "60"});//HQDOP
		SALES_AREA.put("HQEXP", new String[] {"F116", "20", "60"});//HQEXP

		MAPPING_CODE.put("EUDOP_I", "0001075204");//유럽부품
		MAPPING_CODE.put("UKDOP_I", "0001074837");//영국부품
		MAPPING_CODE.put("HQDOP_I", "0001031876");//본사내수부품
		MAPPING_CODE.put("HQEXP_I", "0001040174");//본사수출부품

		USER_GROUP.put("EUDOP", "UG_IVEUDO_P01");//EUDOP
		USER_GROUP.put("UKDOP", "UG_IVUKDO_PGM");//UKDOP
		USER_GROUP.put("HQDOP", "UG_IVHQDO_P09");//HQDOP
		USER_GROUP.put("HQEXP", "UG_IVHQEX_{TZONE}");//HQEXP

		//유럽부품
		AUTHORITY.put("EUDOP_I", new String[] {"DP_EUDO_PARTS_DEALER", "DP_EUDO_PARTS_MANAGER"});//Internal User
		AUTHORITY.put("EUDOP_D", new String[] {"DP_EUDO_PARTS_DEALER"});//Dealer
		AUTHORITY.put("EUDOP_S", new String[] {"DP_EUDO_PARTS_SUB_DEALER"});//Sub Dealer
		AUTHORITY.put("EUDOP_R", new String[] {"DP_EUDO_PARTS_READ_ONLY_DEALER"});//Read-Only Dealer

		//영국부품
		AUTHORITY.put("UKDOP_I", new String[] {"DP_UKDO_PARTS_DEALER", "DP_UKDO_PARTS_MANAGER"});//Internal User
		AUTHORITY.put("UKDOP_D", new String[] {"DP_UKDO_PARTS_DEALER"});//Dealer

		//본사내수부품
		AUTHORITY.put("HQDOP_I", new String[] {"DP_IVDO_PARTS_DEALER", "DP_IVDO_PARTS_MANAGER"});//Internal User
		AUTHORITY.put("HQDOP_D", new String[] {"DP_IVDO_PARTS_DEALER"});//Dealer

		//본사수출부품
		AUTHORITY.put("HQEXP_I", new String[] {"DP_IVEX_PARTS_DEALER", "DP_IVEX_PARTS_MANAGER"});//Internal User
		AUTHORITY.put("HQEXP_D", new String[] {"DP_IVEX_PARTS_DEALER"});//Dealer
	}

	public static String getBizTypeBySalesArea(ErpDataMap userInfoMap)
	{
		return BIZ_TYPE.get(userInfoMap.getSalesOrg() + userInfoMap.getDistChannel());
	}

	public static String[] getSalesArea(String bizType)
	{
		return SALES_AREA.get(bizType);
	}

	public static String getDefaultMappingCode(String bizType, UserType userType)
	{
		return MAPPING_CODE.get(bizType + "_" + userType);
	}

	public static String[] getAuthorities(String bizType, UserType userType)
	{
		return AUTHORITY.get(bizType + "_" + userType);
	}

	public static String getUserGroupName(String bizType, String tzone)
	{
		if (CmStringUtils.equals("HQEXP", bizType))
		{
			return CmStringUtils.replace(USER_GROUP.get(bizType), "{TZONE}", tzone);
		}

		return USER_GROUP.get(bizType);
	}
}
