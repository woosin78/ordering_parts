package org.jwebppy.platform.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.config.CacheConfig;
import org.jwebppy.platform.core.security.authentication.dto.ErpUserContext;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@Component
public class CacheHelper
{
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void evict(Set<String> cacheNames)
	{
		for (String cacheName: CollectionUtils.emptyIfNull(cacheNames))
		{
			evict(cacheName);
		}
	}

	public void evict(String[] cacheNames)
	{
		for (String cacheName: ArrayUtils.nullToEmpty(cacheNames))
		{
			evict(cacheName);
		}
	}

	public void evict(String cacheName)
	{
		if (CmStringUtils.isNotEmpty(cacheName))
		{
			String query = null;

			if (CacheConfig.hasCacheName(cacheName))
			{
				query = cacheName + "::";
			}
			else
			{
				ErpUserContext rrpUserContext = UserAuthenticationUtils.getUserDetails().getErpUserContext();

				if (ObjectUtils.isNotEmpty(rrpUserContext))
				{
					query = cacheName + "::" + rrpUserContext.getSalesOrg() + ":" + rrpUserContext.getCustCode();
				}
			}

			if (CmStringUtils.isNotEmpty(query))
			{
				final ScanOptions options = ScanOptions.scanOptions().match(query + "*").build();

				List<String> keys = redisTemplate.execute(new RedisCallback<List<String>>() {
					@Override
					public List<String> doInRedis(RedisConnection connection) throws DataAccessException
					{
						List<String> keys = new ArrayList<>();

						Cursor<byte[]> cursor = connection.scan(options);

						while (cursor.hasNext())
						{
							keys.add(new String(cursor.next()));
						}

						return keys;
					}

				});

				for (String key: keys)
				{
					cacheManager.getCache(cacheName).evict(CmStringUtils.remove(key, cacheName + "::"));
				}
			}
		}
	}
}
