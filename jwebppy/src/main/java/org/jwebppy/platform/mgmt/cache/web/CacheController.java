package org.jwebppy.platform.mgmt.cache.web;

import org.jwebppy.config.RedisConfig;
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
					@CacheEvict (value = RedisConfig.LANG, allEntries = true),
					@CacheEvict (value = RedisConfig.CITEM, allEntries = true),
					@CacheEvict (value = RedisConfig.USER, allEntries = true),
					@CacheEvict (value = RedisConfig.CUSTOMER, allEntries = true),
					@CacheEvict (value = RedisConfig.ORDER_DISPLAY, allEntries = true),
					@CacheEvict (value = RedisConfig.BACKORDER, allEntries = true),
					@CacheEvict (value = RedisConfig.ORDER_STATUS, allEntries = true),
					@CacheEvict (value = RedisConfig.INVOICE_STATUS, allEntries = true),
					@CacheEvict (value = RedisConfig.SHIPMENT_STATUS, allEntries = true),
					@CacheEvict (value = RedisConfig.PARTS_INFO_AUTOCOMPLETE, allEntries = true)
			})
	public Object resetAll()
	{
		return EMPTY_RETURN_VALUE;
	}
}
