package com.sjw.doran.requestdatagenerator.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.sjw.doran.requestdatagenerator.item.repository",
        entityManagerFactoryRef = "itemEntityManagerFactory",
        transactionManagerRef = "itemTransactionManager")
public class ItemDataSourceConfig {

    @Bean(name = "itemDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.item")
    public DataSource itemDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "itemEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean itemEntityManagerFactory(@Qualifier("itemDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.sjw.doran.requestdatagenerator.item.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "itemTransactionManager")
    public PlatformTransactionManager itemTransactionManager(
            @Qualifier("itemEntityManagerFactory") LocalContainerEntityManagerFactoryBean itemEntityManagerFactory) {
        return new JpaTransactionManager(itemEntityManagerFactory.getObject());
    }
}
