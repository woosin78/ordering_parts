package org.jwebppy.config;

//@Configuration
public class RedisConfig
{
	/*
	@Bean
	public ConfigureRedisAction configureRedisAction()
	{
	    return ConfigureRedisAction.NO_OP;
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory)
	{
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()//null value 캐시안함
                .entryTtl(Duration.ofSeconds(60))//캐시의 기본 유효시간 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));//redis 캐시 데이터 저장방식을 StringSeriallizer로 지정

		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();

		redisCacheConfigurationMap.put(PlatformConfigVo.LANG, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_24_HOURS)));
		redisCacheConfigurationMap.put(PlatformConfigVo.CITEM, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_24_HOURS)));
		redisCacheConfigurationMap.put(PlatformConfigVo.USER, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_24_HOURS)));
		redisCacheConfigurationMap.put(PlatformConfigVo.CUSTOMER, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_5_MINUTES)));
		redisCacheConfigurationMap.put(PlatformConfigVo.ORDER_TYPE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_1_HOUR)));
		redisCacheConfigurationMap.put(PlatformConfigVo.ORDER_DISPLAY, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(PlatformConfigVo.BACKORDER, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(PlatformConfigVo.ORDER_STATUS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(PlatformConfigVo.INVOICE_STATUS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(PlatformConfigVo.SHIPMENT_STATUS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_10_MINUTES)));
		redisCacheConfigurationMap.put(PlatformConfigVo.PARTS_INFO_AUTOCOMPLETE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(PlatformConfigVo.TTL_1_HOUR)));

		return RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(redisCacheConfigurationMap).build();
	}
	*/
}
