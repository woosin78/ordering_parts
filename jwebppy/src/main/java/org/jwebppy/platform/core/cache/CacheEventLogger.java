package org.jwebppy.platform.core.cache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEventLogger implements CacheEventListener<Object, Object>
{
	private Logger logger = LoggerFactory.getLogger(CacheEventLogger.class);

	@Override
	public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent)
	{
		logger.info("cache event logger message. getKey: {" + cacheEvent.getKey() + "} / getOldValue: {" + cacheEvent.getOldValue() + "} / getNewValue:{" + cacheEvent.getNewValue() + "}");
	}
}