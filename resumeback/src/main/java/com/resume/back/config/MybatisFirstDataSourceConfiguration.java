package com.resume.back.config;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@MapperScan(value = "com.resume.back.database.mybatis.first", sqlSessionFactoryRef = "firstSqlSessionFactory")
@EnableJpaRepositories(basePackages = {
		"com.resume.back.database.jpa.entity.first" }, entityManagerFactoryRef = "firstEntityManagerFactory", 
		transactionManagerRef = "firstTransactionManager" 
)
public class MybatisFirstDataSourceConfiguration {

	// Mybatis
	@Primary
	@Bean(name = "firstDataSource")
	@ConfigurationProperties(prefix = "spring.first.datasource")
	public DataSource firstDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean(name = "firstSqlSessionFactory")
	public SqlSessionFactory firstSqlSessionFactory(@Qualifier("firstDataSource") DataSource firstDataSource,
			ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(firstDataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage("com.resume.back.database.model");
		sqlSessionFactoryBean.setMapperLocations(
				applicationContext.getResources("classpath:com/resume/back/database/mybatis/first/**.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Primary
	@Bean(name = "firstSessionTemplate")
	public SqlSessionTemplate firstSqlSessionTemplate(
			@Qualifier("firstSqlSessionFactory") SqlSessionFactory firstSqlSessionFactory) {
		return new SqlSessionTemplate(firstSqlSessionFactory);
	}

	// JPA

	@Primary
	@Bean(name="firstEntityManagerFactory")
	public EntityManagerFactory entityManagerFactory(@Qualifier("firstDataSource") DataSource dataSource) {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.resume.back.database.jpa.entity.first");
		
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		
		final HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName()); // 네이밍
		properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName()); // 네이밍
		em.setJpaPropertyMap(properties);
		em.afterPropertiesSet();

		return em.getObject();
	}

	@Primary
	@Bean(name="firstTransactionManager")
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(entityManagerFactory);
		return tm;
	}

}
