package com.tallerbd.backend;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "seconddbEntityManagerFactory",
    transactionManagerRef = "seconddbTransactionManager", basePackages = {"com.tallerbd.backend.seconddb.repo"})
public class SeconddbDbConfig {

  @Bean(name = "seconddbDataSource")
  @ConfigurationProperties(prefix = "seconddb.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "seconddbEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean seconddbEntityManagerFactory(
      EntityManagerFactoryBuilder builder, @Qualifier("seconddbDataSource") DataSource dataSource) {
    return builder.dataSource(dataSource).packages("com.tallerbd.backend.seconddb.domain").persistenceUnit("seconddb")
        .build();
  }

  @Bean(name = "seconddbTransactionManager")
  public PlatformTransactionManager seconddbTransactionManager(
      @Qualifier("seconddbEntityManagerFactory") EntityManagerFactory seconddbEntityManagerFactory) {
    return new JpaTransactionManager(seconddbEntityManagerFactory);
  }

}
