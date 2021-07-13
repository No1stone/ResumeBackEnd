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
@MapperScan(value = "com.resume.back.database.mybatis.third", sqlSessionFactoryRef = "thirdSqlSessionFactory")
public class MybatisThirdDataSourceConfiguration {
    @Bean(name = "thirdDataSource")
    @ConfigurationProperties(prefix="spring.third.datasource")
    public DataSource ThirdDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "thirdSqlSessionFactory")
    public SqlSessionFactory thirdSqlSessionFactory(@Qualifier("thirdDataSource") DataSource thirdDataSource,
                                                     ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(thirdDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.resume.back.database.model");
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:com/resume/back/database/mybatis/third/**.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    
    @Bean(name = "thirdSessionTemplate")
    public SqlSessionTemplate thirdSqlSessionTemplate(@Qualifier("thirdSqlSessionFactory") SqlSessionFactory thirdSqlSessionFactory) {
        return new SqlSessionTemplate(thirdSqlSessionFactory);
    }
}
