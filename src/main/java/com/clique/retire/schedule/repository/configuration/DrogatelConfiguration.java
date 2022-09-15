package com.clique.retire.schedule.repository.configuration;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * Classe responsável por carregar as configurações de dataSource do DrogatelDB
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackages = "com.clique.retire.schedule.repository", 
	entityManagerFactoryRef = "drogatelEntityManager", 
	transactionManagerRef = "drogatelTransactionManager"
)
public class DrogatelConfiguration {

	@Autowired
	private Environment env;
	
	@Primary
    @Bean(name="drogatelEntityManager")
    public LocalContainerEntityManagerFactoryBean drogatelEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(drogatelDataSource());
        em.setPackagesToScan(new String[] {"com.clique.retire.schedule.model.drogatel"});
 
        HibernateJpaVendorAdapter vendorAdapter  = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
 
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("jpa.hibernate.ddl.auto"));
        properties.put("hibernate.show_sql", env.getProperty("jpa.show.sql"));
        properties.put("hibernate.format_sql", env.getProperty("jpa.format.sql"));
        properties.put("hibernate.dialect", env.getProperty("jpa.dialect")); 
        properties.put("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults", false);
        		
        em.setJpaPropertyMap(properties);
        
        return em;
    }
	
	@Primary
	@Bean(name="drogatelDataSource")
    public DataSource drogatelDataSource() {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	dataSource.setUrl(env.getProperty("ds.drogatel.url"));
        dataSource.setDriverClassName(env.getProperty("sql.driverClassName"));
	    dataSource.setUsername(env.getProperty("sql.drogatel.username"));
	    dataSource.setPassword(env.getProperty("sql.drogatel.password"));
	    return dataSource;
    }
	
	@Primary
	@Bean(name="drogatelTransactionManager")
    public PlatformTransactionManager drogatelTransactionManager() {
       JpaTransactionManager transactionManager = new JpaTransactionManager();
       transactionManager.setEntityManagerFactory(drogatelEntityManager().getObject());
       return transactionManager;
    }
}