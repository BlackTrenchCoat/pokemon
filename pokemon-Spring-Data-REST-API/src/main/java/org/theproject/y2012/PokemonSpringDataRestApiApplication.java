package org.theproject.y2012;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.MediaType;

@SpringBootApplication
public class PokemonSpringDataRestApiApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PokemonSpringDataRestApiApplication.class, args);
    }

    // Use application/json MIME type for REST responses instead of application/hal+json
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurerAdapter() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.setDefaultMediaType(MediaType.APPLICATION_JSON);
            }
        };
    }
    
}