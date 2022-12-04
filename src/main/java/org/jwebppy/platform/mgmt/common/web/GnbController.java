package org.jwebppy.platform.mgmt.common.web;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentAuthorityService;
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

	@GetMapping("/menu")
	@ResponseBody
	public Object menu(@ModelAttribute CItemSearchDto citemSearch)
	{
		List<Map<String, Object>> citemsHierarchy = new LinkedList<>();

		citemSearch.setUseq(getUseq());

		List<CItemDto> citems = contentAuthorityService.getMyCitemHierarchy(citemSearch);

		for (CItemDto citem : citems)
		{
			Map<String, Object> itemMap = new LinkedHashMap<>();
			itemMap.put("KEY", citem.getCseq());
			itemMap.put("NAME", CmStringUtils.defaultIfEmpty(citem.getName2(), citem.getName()));
			itemMap.put("TYPE", citem.getType().toString());
			itemMap.put("URL", CmStringUtils.trimToEmpty(citem.getUrl()));
			itemMap.put("LAUNCH_TYPE", CmStringUtils.trimToEmpty(citem.getLaunchType()));
			itemMap.put("WIDTH", CmStringUtils.trimToEmpty(citem.getScrWidth()));
			itemMap.put("HEIGHT", CmStringUtils.trimToEmpty(citem.getScrHeight()));

			itemMap.put("SUB_ITEMS", getSubItems(citem.getSubCitems()));

			citemsHierarchy.add(itemMap);
		}

		return citemsHierarchy;
	}

	protected List<Map<String, Object>> getSubItems(List<CItemDto> subCitems)
	{
		List<Map<String, Object>> citems = new LinkedList<>();

		if (CollectionUtils.isNotEmpty(subCitems))
		{
			for (CItemDto subCItem: subCitems)
			{
				Map<String, Object> itemMap = new LinkedHashMap<>();
				itemMap.put("KEY", subCItem.getCseq());
				itemMap.put("NAME", CmStringUtils.defaultIfEmpty(subCItem.getName2(), subCItem.getName()));
				itemMap.put("TYPE", subCItem.getType().toString());
				itemMap.put("URL", subCItem.getUrl());
				itemMap.put("LAUNCH_TYPE", CmStringUtils.trimToEmpty(subCItem.getLaunchType()));
				itemMap.put("WIDTH", CmStringUtils.trimToEmpty(subCItem.getScrWidth()));
				itemMap.put("HEIGHT", CmStringUtils.trimToEmpty(subCItem.getScrHeight()));
				itemMap.put("SUB_ITEMS", getSubItems(subCItem.getSubCitems()));

				citems.add(itemMap);
			}
		}

		return citems;
	}
}
