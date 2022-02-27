package org.jwebppy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;

@Configuration
public class RedisConfig
{
	@Bean
	public ConfigureRedisAction configureRedisAction()
	{
	    return ConfigureRedisAction.NO_OP;
	}
}
