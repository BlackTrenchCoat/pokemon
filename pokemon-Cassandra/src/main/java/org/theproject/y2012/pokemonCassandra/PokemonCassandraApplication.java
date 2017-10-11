package org.theproject.y2012.pokemonCassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Component;
import org.theproject.y2012.pokemonCassandra.cassandraDomain.CassandraPokemonEntity;

@SpringBootApplication
public class PokemonCassandraApplication {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    public static void main(String[] args) {
        SpringApplication.run(PokemonCassandraApplication.class, args);
    }

    @Component
    public class ReadMysql implements CommandLineRunner {

        @Autowired
        MysqlPokemonEntityRepository repo;

        @Override
        public void run(String... args) throws Exception {

            repo.findAll().stream().forEach(i -> {
                CassandraPokemonEntity c = new CassandraPokemonEntity(i.getId(), i.getSpeciesId(), i.getHeight(),
                        i.getWeight(), i.getBaseExperience(), i.getOrder(),
                        i.getIsDefault() == 1);
                cassandraTemplate.insert(c);
            });
            System.exit(0);
        }
    }

}
