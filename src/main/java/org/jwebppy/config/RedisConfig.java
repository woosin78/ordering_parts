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

	public static final String ORDER_DISPLAY = "OrderDisplay";
	public static final String PARTS_INFO_AUTOCOMPLETE = "PartsInfoAutoComplete";
	public static final String BACKORDER = "Backorder";
	public static final String ORDER_STATUS = "OrderStatus";
	public static final String ORDER_DISPLAY_DETAIL = "OrderDisplayDetail";
	public static final String INVOICE_STATUS = "InvoiceStatus";
	public static final String SHIPMENT_STATUS = "ShipmentStatus";
	public static final String SHIPMENT_STATUS_DETAIL = "ShipmentStatusDetail";
	
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
