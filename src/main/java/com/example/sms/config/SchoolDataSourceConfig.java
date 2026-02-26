package com.example.sms.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.sms.school.repository",
        entityManagerFactoryRef = "schoolEntityManagerFactory",
        transactionManagerRef = "schoolTransactionManager"
)
public class SchoolDataSourceConfig {

    // DEFAULT school datasource (sch001)
    @Bean
    public DataSource defaultSchoolDataSource() {
        com.zaxxer.hikari.HikariDataSource ds = new com.zaxxer.hikari.HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/sms_school_sch001");
        ds.setUsername("root");
        ds.setPassword("minnu@443");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }

    // ROUTING datasource
    @Bean
    public DataSource schoolRoutingDataSource(
            @Qualifier("defaultSchoolDataSource") DataSource defaultDs) {

        Map<Object, Object> targetDataSources = new HashMap<>();

        // REQUIRED mappings
        targetDataSources.put("DEFAULT", defaultDs);
        targetDataSources.put("SCH001", defaultDs);
        targetDataSources.put("SCH002", createSchoolDataSource("sms_school_sch002"));
        targetDataSources.put("SCH003", createSchoolDataSource("sms_school_sch003"));

        SchoolRoutingDataSource routingDataSource = new SchoolRoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(defaultDs);
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    private DataSource createSchoolDataSource(String dbName) {
        com.zaxxer.hikari.HikariDataSource ds = new com.zaxxer.hikari.HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/" + dbName);
        ds.setUsername("root");
        ds.setPassword("minnu@443");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean schoolEntityManagerFactory(
            @Qualifier("schoolRoutingDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.sms.school.entity");
        emf.setPersistenceUnitName("schoolPU");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        emf.setJpaPropertyMap(props);
        return emf;
    }

    @Bean
    public PlatformTransactionManager schoolTransactionManager(
            @Qualifier("schoolEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}