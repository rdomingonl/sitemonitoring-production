package net.sf.sitemonitoring;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import net.sf.sitemonitoring.annotation.StandaloneProfile;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

//@StandaloneProfile
@Configuration
public class DatabaseConfiguration {
	Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

//	@Value("${dbport}")
//	private int dbPort;
	@Value("${dbUrl}")
	String dbUrl;
	@Value("${dbUsername}")
	String dbUsername;
	@Value("${dbPassword}")
	String dbPassword;
	@Value("${dbDriver}")
	String dbDriver;
	@Value("${dbDialect}")
	String dbDialect;
	
	@PostConstruct
	private void logSettings() {
		log.info("dbUrl      : "+dbUrl);
		log.info("dbUsername : "+dbUsername);
		log.info("dbDriver   : "+dbDriver);
		log.info("dbDialect  : "+dbDialect);
	}
		
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", "update");
		jpaProperties.put("hibernate.show_sql", "false");
		jpaProperties.put("hibernate.dialect", dbDialect);

		entityManagerFactory.setJpaProperties(jpaProperties);
		entityManagerFactory.setPackagesToScan("net.sf.sitemonitoring.entity");
		entityManagerFactory.setPersistenceProvider(new HibernatePersistenceProvider());
		return entityManagerFactory;
	}

	@Bean
	public JpaTransactionManager transactionManager(DataSource dataSource, EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}

	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(dbDriver);
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(dbPassword);
		return dataSource;
	}

}
