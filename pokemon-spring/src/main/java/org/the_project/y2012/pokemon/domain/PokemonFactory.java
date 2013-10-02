package org.the_project.y2012.pokemon.domain;

import static org.the_project.y2012.pokemon.domain.PokemonMath.rnd;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.the_project.y2012.pokemon.model.HeightWeight;
import org.the_project.y2012.pokemon.model.PokedexDAO;
import org.the_project.y2012.pokemon.model.SpeciesName;

@Transactional
public class PokemonFactory {
    
    private static Logger logger = Logger.getLogger("PokemonFactory");
    
    static List<pokedex.PokemonSpecies> pokemonList = null;

    public Pokemon randomPokemon(PokedexDAO pokedexDAO) {
        Pokemon result = null;

        // First time, initialize the Pokemon list
        if (pokemonList == null) {
            pokemonList = pokedexDAO.getGen1AndGen2Pokemon();
        }

        int index = rnd(pokemonList.size());
        pokedex.PokemonSpecies randomPokemon = (pokedex.PokemonSpecies) pokemonList.get(index);

        result = new Pokemon();

        result.pokedexId = randomPokemon.getId();
        
        HeightWeight heightWeight = pokedexDAO.getHeightWeight(result.pokedexId);
        result.height = heightWeight.getHeight();
        result.weight = heightWeight.getWeight();

        // TODO: These are always the same?
        // No, they shouldn't be the same, e.g. name=Golduck, species=Duck
        // POKEMON_SPECIES.IDENTIFIER does look like the 'name'. Do I actually
        // use the 'species' string for anything?  
        // A: See POKEMON_SPECIES_NAMES.GENUS for what used to be POKEMON.SPECIES.
        SpeciesName speciesName = pokedexDAO.getSpeciesName(result.pokedexId);
        result.name = speciesName.getName();
        result.species = speciesName.getSpecies();
        
        logger.info("pokemon id = " + result.pokedexId + ", species = " + result.species
                + ", name = " + result.name);

        // Types

        String[] types = pokedexDAO.getTypes(result.pokedexId);
        result.type1 = types[0];
        result.type2 = types[1];

        // Colors
        
        result.color = pokedexDAO.getColor(result.pokedexId);

        // Habitats

        // Stats (attack, defense, special attack, special defense, speed, hp)

        Map<String, Integer> stats = pokedexDAO.getStats(result.pokedexId);
        for (String key : stats.keySet()) {
            int value = (Integer) stats.get(key);
            if (key.equals("hp")) {
                result.hp = value;
                result.maxHp = value;
            } else if (key.equals("attack")) {
                result.attack = value;
            } else if (key.equals("defense")) {
                result.defense = value;
            } else if (key.equals("special-attack")) {
                result.specialAttack = value;
            } else if (key.equals("special-defense")) {
                result.specialDefense = value;
            } else if (key.equals("speed")) {
                result.speed = value;
            }
        }

        result.level = 5;
        result.exp = 0;
        
        Set<Move> moves= pokedexDAO.findPossibleMoves(result.pokedexId, result.level);
        for (Move move : moves) {
            
            // DEBUG
            if ((result.getMoves() != null) && result.getMoves().contains(move)) {
                logger.info("Getting ready to try to add a move to a set that already contains that move...");
            }
            
            result.addMove(move);
        }

        return result;
    }
    
    
}
