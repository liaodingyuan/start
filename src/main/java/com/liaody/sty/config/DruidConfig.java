package com.liaody.sty.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import com.google.common.collect.Sets;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource(){
        return new DruidDataSource();
    }

//    @Bean("sqlSessionFactory")
//    public SqlSessionFactoryBean createSqlSessionFactory(DataSource dataSource){
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        Interceptor[] plugins = new Interceptor[1];
//        plugins[0]=new PageInterceptor();
//        sqlSessionFactoryBean.setPlugins(plugins);
//        return sqlSessionFactoryBean;
//    }
}
