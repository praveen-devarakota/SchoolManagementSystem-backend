package com.example.sms.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.sms.school.repository",
        entityManagerFactoryRef = "schoolEntityManagerFactory",
        transactionManagerRef = "schoolTransactionManager"
)
public class SchoolDataSourceConfig {

    private final DataSource masterDataSource;

    public SchoolDataSourceConfig(
            @Qualifier("masterDataSource") DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    @Bean(name = "schoolRoutingDataSource")
    public DataSource schoolRoutingDataSource() {
        return new SchoolRoutingDataSource(masterDataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean schoolEntityManagerFactory(
            @Qualifier("schoolRoutingDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean emf =
                new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.sms.school.entity");
        emf.setPersistenceUnitName("schoolPU");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return emf;
    }

    @Bean
    public PlatformTransactionManager schoolTransactionManager(
            @Qualifier("schoolEntityManagerFactory")
            EntityManagerFactory emf) {

        return new JpaTransactionManager(emf);
    }
}