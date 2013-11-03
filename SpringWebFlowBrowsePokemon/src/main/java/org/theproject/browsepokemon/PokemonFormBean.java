package org.theproject.browsepokemon;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PokemonFormBean implements Serializable {
	
    private static final long serialVersionUID = 8561844275200010676L;
    
    private static Logger logger = LoggerFactory.getLogger(PokemonFormBean.class);
    
    Set<String> generations;
    Map<String, String> pokemonGenerationNames;
    Map<Integer, String> species;
    String speciesId;

    public Set<String> getGenerations() {
        return generations;
    }

    public void setGenerations(Set<String> generations) {
        this.generations = generations;
        logger.info("Set generations.");
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

    public String getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(String speciesId) {
        this.speciesId = speciesId;
    }

}
