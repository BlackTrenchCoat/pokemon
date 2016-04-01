package org.the_project.y2012.pokemon.configuration;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource({ "classpath:hibernate.properties"})
public class ApplicationContext {
    
    @Autowired
    private Environment env;
    
    @Bean
    public BasicDataSource pokedexDataSource() {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
        result.setUrl(env.getProperty("pokedex.connection.url"));
        result.setUsername(env.getProperty("pokedex.connection.username"));
        result.setPassword(env.getProperty("pokedex.connection.password"));
        result.setInitialSize(5);
        result.setMaxActive(10);
        return result;
    }

    @Bean
    public BasicDataSource pokemonDataSource() {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
        result.setUrl(env.getProperty("pokemon.connection.url"));
        result.setUsername(env.getProperty("pokemon.connection.username"));
        result.setPassword(env.getProperty("pokemon.connection.password"));
        result.setInitialSize(5);
        result.setMaxActive(10);
        return result;
    }
    
}
