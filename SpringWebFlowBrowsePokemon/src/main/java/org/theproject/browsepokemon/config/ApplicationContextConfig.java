package org.theproject.browsepokemon.config;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.theproject.browsepokemon.model.PokedexDAO;
import org.theproject.browsepokemon.model.PokedexDAOImpl;
import org.theproject.browsepokemon.service.PokedexService;
import org.theproject.browsepokemon.service.PokedexServiceImpl;

@Configuration
@PropertySource({"classpath:hibernate.properties"})
@ComponentScan(basePackages = "org.theproject.browsepokemon")
@EnableTransactionManagement
public class ApplicationContextConfig {
    
    private static transient Logger logger = LoggerFactory.getLogger(ApplicationContextConfig.class);

    @Autowired
    private Environment env;
    
    // Do exception translation for components annotated @Repository
    @Bean
    public BeanPostProcessor persistenceTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    // Database-related
    
    // Service
    @Bean
    public PokedexService pokedexService() {
        return new PokedexServiceImpl();
    }
    
    // DAO
    @Bean
    public PokedexDAO pokedexDAO() {
        return new PokedexDAOImpl();
    }
    
    // Session factory
    @Bean
    public AnnotationSessionFactoryBean getSessionFactory() {
        logger.info("*** In getSessionFactory...");
        AnnotationSessionFactoryBean result = new AnnotationSessionFactoryBean();
        result.setDataSource(pokedexDataSource());
        result.setPackagesToScan("pokedex");
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("dialect", env.getProperty("hibernate.dialect"));
        result.setHibernateProperties(hibernateProperties);
        logger.info("*** getSessionFactory: result = " + result);
        return result;
    }
    
    // Data source
    @Bean
    public BasicDataSource pokedexDataSource() {
        logger.info("*** In pokedexDataSource...");
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
        result.setUrl(env.getProperty("hibernate.connection.url"));
        result.setUsername(env.getProperty("hibernate.connection.username"));
        result.setPassword(env.getProperty("hibernate.connection.password"));
        result.setInitialSize(5);
        result.setMaxActive(10);
        return result;
    }
    
    // Transaction manager
    @Bean
    public HibernateTransactionManager pokedexTransactionManager() {
        logger.info("*** In pokedexTransactionManager...");
        HibernateTransactionManager result = new HibernateTransactionManager();
        SessionFactory sessionFactory = getSessionFactory().getObject();
        logger.info("pokedexTransactionManager: sessionFactory = " + sessionFactory);
        result.setSessionFactory(sessionFactory);
        return result;
    }
    

}
