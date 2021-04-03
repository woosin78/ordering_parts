package org.jwebppy.config;

//@Configuration
public class CacheConfig
{
	public static final String LANG = "Language";
	public static final String CITEM = "Content";
	public static final String USER = "User";
	public static final String CUSTOMER = "Customer";
	public static final String ORDER_TYPE = "OrderType";
	public static final String ORDER_DISPLAY = "OrderDisplay";
	public static final String ORDER_DISPLAY_DETAIL = "OrderDisplayDetail";
	public static final String BACKORDER = "Backorder";
	public static final String ORDER_STATUS = "OrderStatus";
	public static final String INVOICE_STATUS = "InvoiceStatus";
	public static final String SHIPMENT_STATUS = "ShipmentStatus";
	public static final String PARTS_INFO_AUTOCOMPLETE = "PartsInfoAutoComplete";

	public static final int TTL_5_MINUTES = 5*60;
	public static final int TTL_10_MINUTES = 10*60;
	public static final int TTL_1_HOUR = 10*60*60;
	public static final int TTL_24_HOURS = 24*60*60;

	/*
	 * This function should be active in case of Spring Cache with Redis
	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory)
	{
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()//null value 캐시안함
                .entryTtl(Duration.ofSeconds(60))//캐시의 기본 유효시간 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));//redis 캐시 데이터 저장방식을 StringSeriallizer로 지정

		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();

		redisCacheConfigurationMap.put(LANG, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_24_HOURS)));
		redisCacheConfigurationMap.put(CITEM, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_24_HOURS)));
		redisCacheConfigurationMap.put(USER, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_24_HOURS)));
		redisCacheConfigurationMap.put(CUSTOMER, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_5_MINUTES)));
		redisCacheConfigurationMap.put(ORDER_TYPE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_1_HOUR)));
		redisCacheConfigurationMap.put(ORDER_DISPLAY, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(BACKORDER, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(ORDER_STATUS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(INVOICE_STATUS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(SHIPMENT_STATUS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(PARTS_INFO_AUTOCOMPLETE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TTL_1_HOUR)));

		return RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(redisCacheConfigurationMap).build();
	}
	*/
}
