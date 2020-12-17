package org.jwebppy.config;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.jwebppy.platform.interceptor.JdbcExecutionInterceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "org.jwebppy")
public class MybatisConfig
{
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception
	{
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setDefaultExecutorType(ExecutorType.BATCH);
		configuration.setJdbcTypeForNull(JdbcType.NULL);

		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setVfs(SpringBootVFS.class);
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/**/*.xml"));
		sessionFactory.setTypeAliasesPackage("org.jwebppy");
		sessionFactory.setPlugins(new Interceptor[] { new JdbcExecutionInterceptor() });
		sessionFactory.setConfiguration(configuration);

		return sessionFactory.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception
	{
		final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);

		return sqlSessionTemplate;
	}
}
