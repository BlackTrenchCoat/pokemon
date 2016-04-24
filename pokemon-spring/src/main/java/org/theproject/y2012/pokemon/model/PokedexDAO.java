package org.theproject.y2012.pokemon.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.theproject.y2012.pokemon.domain.Move;

import pokedex.Pokemon;
import pokedex.PokemonSpecies;

/**
 * 
 * @author lmulcahy
 *
 */
public interface PokedexDAO {

    public List<PokemonSpecies> getGen1AndGen2Pokemon();

    public String[] getTypes(int id);

    public Map<String, Integer> getStats(int id);

    public Set<Move> findPossibleMoves(int pokemonPokedexId, int level);
    
    public String getName(Pokemon pokemon);
    
    public SpeciesName getSpeciesName(int id);
    
    public HeightWeight getHeightWeight(int id);
    
    public String getColor(int id);

}
