package org.theproject.browsepokemon;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class PokemonFormBean implements Serializable {
	
    private static final long serialVersionUID = 8561844275200010676L;
    
    Set<String> generations;
    Map<String, String> pokemonGenerationNames;

    public Set<String> getGenerations() {
        return generations;
    }

    public void setGenerations(Set<String> generations) {
        this.generations = generations;
    }

    public Map<String, String> getPokemonGenerationNames() {
        return pokemonGenerationNames;
    }

    public void setPokemonGenerationNames(Map<String, String> pokemonGenerationNames) {
        this.pokemonGenerationNames = pokemonGenerationNames;
    }

}
