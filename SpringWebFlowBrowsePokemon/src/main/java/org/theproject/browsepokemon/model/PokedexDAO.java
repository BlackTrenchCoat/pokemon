package org.theproject.browsepokemon.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.theproject.browsepokemon.PokemonDisplayObject;

import pokedex.PokemonSpecies;

public interface PokedexDAO {

	public Map<String, String> getPokemonGenerationNames();
	
    public List<PokemonSpecies> getPokemonSpeciesForGenerations(Set<java.lang.String> generations);

    public PokemonDisplayObject getDisplayObject(Integer id);
}
