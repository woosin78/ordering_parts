package org.jwebppy.platform.mgmt.logging.web;

import org.apache.commons.collections.CollectionUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogAuthorityChecker
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	public boolean hasRead()
	{
		return (hasAuthority(new String[] {"DP_SAP_RFC_LOG_READ", "DP_SAP_RFC_LOG_EXECUTE"}) || UserAuthenticationUtils.hasRole(new String[] {"DP_SAP_RFC_LOG_READ", "DP_SAP_RFC_LOG_EXECUTE"}));
	}

	public boolean hasWrite()
	{
		return (hasAuthority(new String[] {"DP_SAP_RFC_LOG_EXECUTE"}) || UserAuthenticationUtils.hasRole(new String[] {"DP_SAP_RFC_LOG_EXECUTE"}));
	}

	public boolean hasAuthority(String[] authorities)
	{
		CItemSearchDto cItemSearch = new CItemSearchDto();
		cItemSearch.setUsername(UserAuthenticationUtils.getUserDetails().getRealUsername());
		cItemSearch.setNames(authorities);

		return CollectionUtils.isNotEmpty(contentAuthorityService.getMyCItems(cItemSearch));
	}
}
