package org.jwebppy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

@Configuration
public class RedisConfig
{
	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	@Bean
	public RedisTemplate<?, ?> redisTemplate()
	{
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

	@Bean
	public ConfigureRedisAction configureRedisAction()
	{
	    return ConfigureRedisAction.NO_OP;
	}
}
