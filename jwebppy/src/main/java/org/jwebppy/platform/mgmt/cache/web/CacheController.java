package org.jwebppy.platform.mgmt.cache.web;

import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cache")
public class CacheController extends MgmtGeneralController
{
	@RequestMapping("/reset/all")
	@ResponseBody
	@Caching(
			evict = {
					@CacheEvict (value = CacheConfig.LANG, allEntries = true),
					@CacheEvict (value = CacheConfig.CITEM, allEntries = true),
					@CacheEvict (value = CacheConfig.USER, allEntries = true)
			})
	public Object resetAll()
	{
		return EMPTY_RETURN_VALUE;
	}
}
