package org.the_project.y2012.pokemon;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.the_project.y2012.pokemon.domain.Pokemon;
import org.the_project.y2012.pokemon.domain.PokemonFactory;
import org.the_project.y2012.pokemon.model.PokedexDAO;
import org.the_project.y2012.pokemon.model.PokemonDAO;

/**
 * Main program to create an initial population of pokemon and persist them to
 * the database.
 * 
 * @author lmulcahy
 * 
 */
public class InitializePopulation {

    private static Logger logger = Logger.getLogger("InitializePopulation");

    final static private int HOWMANY = 100;

    public static void main(String[] args) {
        
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "/META-INF/spring/applicationContext.xml");
        
        PokedexDAO pokedexDAO = (PokedexDAO)applicationContext.getBean("pokedexDAO");
        PokemonDAO pokemonDAO = (PokemonDAO)applicationContext.getBean("pokemonDAO");
        PokemonFactory pokemonFactory = new PokemonFactory();

        Pokemon[] population = new Pokemon[HOWMANY];

        // Read pokedex; generate N random pokemon
        for (int i = 0; i < HOWMANY; i++) {
            population[i] = pokemonFactory.randomPokemon(pokedexDAO);
        }

        // Persist N pokemon to the database
        for (int i = 0; i < HOWMANY; i++) {
            pokemonDAO.save(population[i]);
        }

        logger.info("Done");

    }

}
