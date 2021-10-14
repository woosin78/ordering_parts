package org.jwebppy;

import org.jwebppy.platform.core.support.CmBeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableAspectJAutoProxy
@ComponentScan(nameGenerator = CmBeanNameGenerator.class)
public class JwebppyApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(JwebppyApplication.class, args);
	}

}
