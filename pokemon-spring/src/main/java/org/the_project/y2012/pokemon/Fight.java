package org.the_project.y2012.pokemon;

import static org.the_project.y2012.pokemon.domain.PokemonMath.rnd;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.the_project.y2012.pokemon.domain.Move;
import org.the_project.y2012.pokemon.domain.Pokemon;
import org.the_project.y2012.pokemon.model.PokemonDAO;

/**
 * 
 * Main program to read persisted pokemons out of the database and have them
 * fight each other
 * 
 * @author lmulcahy
 * 
 */
public class Fight {
    
    private static Logger logger = Logger.getLogger("Fight");
    
    public static void main(String[] args) {
        
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "/META-INF/spring/applicationContext.xml");
        
        PokemonDAO pokemonDAO = (PokemonDAO)applicationContext.getBean("pokemonDAO");
        
        List<Pokemon> population = pokemonDAO.getAllPokemon();
        
        int big = population.size();
        
        logger.info("Got " + big + " pokemon from the database");
        
        // Choose 2 randomly
        int i1 = rnd(big);
        int i2 = i1;
        do {
            i2 = rnd(big);
        } while (i1 == i2);

        Pokemon[] pokemon = new Pokemon[2];
        pokemon[0] = population.get(i1);
        pokemon[1] = population.get(i2);

        for (int i = 0; i < 2; i++) {
            logger.info(pokemon[i]);

            Set<Move> moves = pokemon[i].getMoves();
            logger.info(moves.size() + " possible moves.");
            for (Move move : moves) {
                logger.info(move);
            }
        }
        
        Pokemon.battle(pokemon[0], pokemon[1]);
    }

}
