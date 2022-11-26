package org.jwebppy.platform.core.security;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

public class EncryptionEnvironmentPostProcessor implements EnvironmentPostProcessor
{
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application)
	{
		Properties props = new Properties();

		try
		{
			props.put("spring.datasource.password", AES256Cipher.getInstance().decode(environment.getProperty("spring.datasource.password")));
			props.put("spring.datasource.username", AES256Cipher.getInstance().decode(environment.getProperty("spring.datasource.username")));
			props.put("spring.mail.username", AES256Cipher.getInstance().decode(environment.getProperty("spring.mail.username")));
			props.put("spring.mail.password", AES256Cipher.getInstance().decode(environment.getProperty("spring.mail.password")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		environment.getPropertySources().addFirst(new PropertiesPropertySource("myProps", props));
	}

	public static void main(String[] args)
	{
		System.out.println(AES256Cipher.getInstance().encode("divdp"));
		System.out.println(AES256Cipher.getInstance().encode("1111"));
	}
}
