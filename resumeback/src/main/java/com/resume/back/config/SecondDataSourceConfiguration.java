package com.resume.back.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(value = "com.resume.back.mybatis.mapper.second", sqlSessionFactoryRef = "secondSqlSessionFactory")
public class SecondDataSourceConfiguration {

	@Bean(name = "secondDataSource")
    @ConfigurationProperties(prefix="spring.second.datasource")
    public DataSource SecondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondSqlSessionFactory")
    public SqlSessionFactory secondSqlSessionFactory(@Qualifier("secondDataSource") DataSource secondDataSource,
                                                          ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(secondDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.resume.back.mybatis.model");
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:com/resume/back/mybatis/mapper/second/**.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "secondSessionTemplate")
    public SqlSessionTemplate secondSqlSessionTemplate(@Qualifier("secondSqlSessionFactory") SqlSessionFactory secondSqlSessionFactory) {
        return new SqlSessionTemplate(secondSqlSessionFactory);
    }
	
}
