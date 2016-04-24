package org.theproject.y2012.pokemon.model;

import java.util.List;

import org.theproject.y2012.pokemon.domain.Pokemon;

/**
 * 
 * @author lmulcahy
 *
 */
public interface PokemonDAO {

    public void save(Object o);
    
    public List<Pokemon> getAllPokemon();
    
}
