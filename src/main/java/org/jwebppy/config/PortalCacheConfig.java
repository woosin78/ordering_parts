package org.jwebppy.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jwebppy.platform.core.cache.PortalCacheKeyGenerator;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class PortalCacheConfig
{
	public static final String CUSTOMER = "Customer";
	public static final String ORDER_TYPE = "OrderType";
	public static final String ORDER_DISPLAY = "OrderDisplay";
	public static final String BACKORDER = "Backorder";
	public static final String ORDER_STATUS = "OrderStatus";
	public static final String ORDER_DISPLAY_DETAIL = "OrderDisplayDetail";
	public static final String INVOICE_STATUS = "InvoiceStatus";
	public static final String SHIPMENT_STATUS = "ShipmentStatus";
	public static final String SHIPMENT_STATUS_DETAIL = "ShipmentStatusDetail";
	public static final String DELIVERY_STATUS = "DeliveryStatus";
	public static final String AP_LIST = "ApList";
	public static final String AP_DETAIL = "ApDetail";
	public static final String AP_SCHEDULE = "ApShedule";
	public static final String AR_LIST = "ArList";
	public static final String CLAIM_DISPLAY = "ClaimDisplay";
	public static final String CLAIM_DISPLAY_DETAIL = "ClaimDisplayDetail";
	public static final String CLAIM_REASON = "ClaimReason";
	public static final String PARTS_INFO_AUTOCOMPLETE = "PartsInfoAutoComplete";
	public static final String DEALER_INFO_AUTOCOMPLETE = "DealerInfoAutoComplete";
	public static final String BUSINESS_TOOLS = "BusinessTools";

	public static final String EX_ORDER_DISPLAY = "ExOrderDisplay";
	public static final String EX_BACKORDER = "ExBackorder";
	public static final String EX_ORDER_STATUS = "ExOrderStatus";

	public static final int TTL_1_MINUTES = 60;
	public static final int TTL_5_MINUTES = 5*60;
	public static final int TTL_10_MINUTES = 10*60;
	public static final int TTL_20_MINUTES = 20*60;
	public static final int TTL_1_HOUR = 10*60*60;
	public static final int TTL_24_HOURS = 24*60*60;

	private static Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();

	/*
	 * This function should be active in case of Spring Cache with Redis
	 */
	@Bean
	public RedisCacheManager portalCacheManager(RedisConnectionFactory redisConnectionFactory)
	{
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()//null value 캐시안함
                .entryTtl(Duration.ofSeconds(TTL_1_MINUTES))//캐시의 기본 유효시간 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));//redis 캐시 데이터 저장방식을 StringSeriallizer로 지정

		redisCacheConfigurationMap.put(CUSTOMER, getTtl(TTL_10_MINUTES));
		redisCacheConfigurationMap.put(ORDER_TYPE, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(ORDER_DISPLAY, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(BACKORDER, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(ORDER_STATUS, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(ORDER_DISPLAY_DETAIL, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(INVOICE_STATUS, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(SHIPMENT_STATUS, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(SHIPMENT_STATUS_DETAIL, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(DELIVERY_STATUS, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(AP_LIST, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(AP_DETAIL, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(AP_SCHEDULE, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(AR_LIST, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(CLAIM_DISPLAY, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(CLAIM_REASON, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(PARTS_INFO_AUTOCOMPLETE, getTtl(TTL_24_HOURS));
		redisCacheConfigurationMap.put(DEALER_INFO_AUTOCOMPLETE, getTtl(TTL_24_HOURS));
		redisCacheConfigurationMap.put(BUSINESS_TOOLS, getTtl(TTL_1_HOUR));

		redisCacheConfigurationMap.put(EX_ORDER_DISPLAY, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(EX_BACKORDER, getTtl(TTL_20_MINUTES));
		redisCacheConfigurationMap.put(EX_ORDER_STATUS, getTtl(TTL_20_MINUTES));

		return RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(redisCacheConfigurationMap).build();
	}

	private RedisCacheConfiguration getTtl(int ttl)
	{
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds((ttl)));
	}

	@Bean
	public KeyGenerator portalCacheKeyGenerator()
	{
		return new PortalCacheKeyGenerator();
	}

	public static boolean hasCacheName(String cacheName)
	{
		return redisCacheConfigurationMap.containsKey(cacheName);
	}

	public Set<String> getCacheNames()
	{
		return redisCacheConfigurationMap.keySet();
	}
}
