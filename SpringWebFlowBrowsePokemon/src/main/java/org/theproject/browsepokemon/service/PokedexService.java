package org.theproject.browsepokemon.service;

import java.util.Map;
import java.util.Set;

public interface PokedexService {

    public Map<String, String> getPokemonGenerationNames();

    public Map<Integer, String> getPokemonSpeciesNames(Set<java.lang.String> generations);

}
