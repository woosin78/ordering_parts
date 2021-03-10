package org.jwebppy.platform.core.support;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

public class CmBeanNameGenerator implements BeanNameGenerator
{
	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry)
	{
		return ((AnnotatedBeanDefinition)definition).getMetadata().getClassName();

		//return definition.getBeanClassName();
	}
}
