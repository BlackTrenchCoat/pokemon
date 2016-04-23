package org.theproject.browsepokemon.config;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
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
@ComponentScan("org.theproject.browsepokemon")
@EnableTransactionManagement
public class ApplicationContextConfig {
    
    private static transient Logger logger = LoggerFactory.getLogger(ApplicationContextConfig.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer
    propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Value("${hibernate.connection.driver_class}") 
    private String dbDriver;
    @Value("${hibernate.connection.url}") 
    private String dbUrl;
    @Value("${hibernate.connection.username}") 
    private String dbUser;
    @Value("${hibernate.connection.password}") 
    private String dbPassword;
    @Value("${hibernate.dialect}")
    private String dbDialect;
    
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
        AnnotationSessionFactoryBean result = new AnnotationSessionFactoryBean();
        result.setDataSource(pokedexDataSource());
        result.setPackagesToScan("pokedex");
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("dialect", dbDialect);
        result.setHibernateProperties(hibernateProperties);
        return result;
    }
    
    // Data source
    @Bean
    public BasicDataSource pokedexDataSource() {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(dbDriver);
        result.setUrl(dbUrl);
        result.setUsername(dbUser);
        result.setPassword(dbPassword);
        result.setInitialSize(5);
        result.setMaxActive(10);
        return result;
    }
    
    // Transaction manager
    @Bean
    public HibernateTransactionManager pokedexTransactionManager() {
        HibernateTransactionManager result = new HibernateTransactionManager();
        SessionFactory sessionFactory = getSessionFactory().getObject();
        result.setSessionFactory(sessionFactory);
        return result;
    }
    

}
