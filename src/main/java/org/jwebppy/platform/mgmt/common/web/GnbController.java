package org.jwebppy.platform.mgmt.common.web;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
import org.jwebppy.platform.mgmt.i18n.service.LangService;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({PlatformConfigVo.CONTEXT_PATH + "/mgmt/gnb", PortalConfigVo.CONTEXT_PATH + "/mgmt/gnb"})
public class GnbController extends MgmtGeneralController
{
	@Autowired
	private ContentAuthorityService contentAuthorityService;

	@Autowired
	private LangService langService;

	@GetMapping("/menu")
	@ResponseBody
	public Object myMenu(@ModelAttribute CItemSearchDto cItemSearch)
	{
		List<Map<String, Object>> cItemsHierarchy = new LinkedList<>();

		cItemSearch.setUSeq(getUSeq());

		List<CItemDto> cItems = contentAuthorityService.getMyCItemHierarchy(cItemSearch);

		for (CItemDto cItem : cItems)
		{
			Map<String, Object> itemMap = new LinkedHashMap<>();
			itemMap.put("KEY", cItem.getCSeq());
			itemMap.put("NAME", CmStringUtils.defaultIfEmpty(cItem.getName2(), cItem.getName()));
			itemMap.put("TYPE", cItem.getType().toString());
			itemMap.put("URL", CmStringUtils.trimToEmpty(cItem.getUrl()));
			itemMap.put("LAUNCH_TYPE", CmStringUtils.trimToEmpty(cItem.getLaunchType()));
			itemMap.put("WIDTH", CmStringUtils.trimToEmpty(cItem.getScrWidth()));
			itemMap.put("HEIGHT", CmStringUtils.trimToEmpty(cItem.getScrHeight()));

			itemMap.put("SUB_ITEMS", getSubItems(cItem.getSubCItems()));

			cItemsHierarchy.add(itemMap);
		}

		return cItemsHierarchy;
	}

	protected List<Map<String, Object>> getSubItems(List<CItemDto> subCItems)
	{
		List<Map<String, Object>> cItems = new LinkedList<>();

		if (CollectionUtils.isNotEmpty(subCItems))
		{
			for (CItemDto subCItem: subCItems)
			{
				Map<String, Object> itemMap = new LinkedHashMap<>();
				itemMap.put("KEY", subCItem.getCSeq());
				itemMap.put("NAME", langService.getCItemText(PlatformConfigVo.DEFAULT_BASENAME, subCItem.getCSeq(), UserAuthenticationUtils.getUserDetails().getLanguage()));
				itemMap.put("TYPE", subCItem.getType().toString());
				itemMap.put("URL", subCItem.getUrl());
				itemMap.put("LAUNCH_TYPE", CmStringUtils.trimToEmpty(subCItem.getLaunchType()));
				itemMap.put("WIDTH", CmStringUtils.trimToEmpty(subCItem.getScrWidth()));
				itemMap.put("HEIGHT", CmStringUtils.trimToEmpty(subCItem.getScrHeight()));
				itemMap.put("SUB_ITEMS", getSubItems(subCItem.getSubCItems()));

				cItems.add(itemMap);
			}
		}

		return cItems;
	}
}
