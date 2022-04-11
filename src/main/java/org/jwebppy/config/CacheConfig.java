package org.jwebppy.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jwebppy.platform.core.cache.CacheKeyGenerator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig
{
	public static final String LANG = "Language";
	public static final String CITEM = "Content";
	public static final String USER = "User";

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
	@Primary
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory)
	{
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()//null value 캐시안함
                .entryTtl(Duration.ofSeconds(60))//캐시의 기본 유효시간 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));//redis 캐시 데이터 저장방식을 StringSeriallizer로 지정

		redisCacheConfigurationMap.put(LANG, getTtl(TTL_24_HOURS));
		redisCacheConfigurationMap.put(CITEM, getTtl(TTL_24_HOURS));
		redisCacheConfigurationMap.put(USER, getTtl(TTL_5_MINUTES));

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
	@Primary
	public KeyGenerator cacheKeyGenerator()
	{
		return new CacheKeyGenerator();
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
