package org.theproject.browsepokemon.service;

import java.util.Map;
import java.util.Set;

import org.theproject.browsepokemon.PokemonDisplayObject;

public interface PokedexService {

    public Map<String, String> getPokemonGenerationNames();

    public Map<Integer, String> getPokemonSpeciesNames(Set<java.lang.String> generations);

    public PokemonDisplayObject getDisplayObject(Integer id);
}
