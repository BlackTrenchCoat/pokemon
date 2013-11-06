package org.theproject.browsepokemon;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class PokemonFormBean implements Serializable {
	
    private static final long serialVersionUID = 8561844275200010676L;
    
    Set<String> generations;
    Map<String, String> pokemonGenerationNames;
    Map<Integer, String> species;
    Integer speciesId;
    PokemonDisplayObject pokemonDisplayObject;

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
    public Map<Integer, String> getSpecies() {
        return species;
    }
    public void setSpecies(Map<Integer, String> species) {
        this.species = species;
    }
    public Integer getSpeciesId() {
        return speciesId;
    }
    public void setSpeciesId(Integer speciesId) {
        this.speciesId = speciesId;
    }
    public PokemonDisplayObject getPokemonDisplayObject() {
        return pokemonDisplayObject;
    }
    public void setPokemonDisplayObject(PokemonDisplayObject pokemonDisplayObject) {
        this.pokemonDisplayObject = pokemonDisplayObject;
    }

}