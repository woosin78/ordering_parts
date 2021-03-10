package org.jwebppy;

import org.jwebppy.platform.core.support.CmBeanNameGenerator;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableAsync
@EnableAspectJAutoProxy
@EnableCaching
@EnableJdbcHttpSession//@EnableRedisHttpSession
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//스프링 시큐리티의 메소드 어노테이션 기반 시큐리티를 활성화 하기 위해서 필요
@EnableBatchProcessing
@EnableScheduling
@ComponentScan(nameGenerator = CmBeanNameGenerator.class)
public class JwebppyApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(JwebppyApplication.class, args);
	}

}
