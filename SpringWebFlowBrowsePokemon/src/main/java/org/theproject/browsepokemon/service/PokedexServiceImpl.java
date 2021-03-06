package org.theproject.browsepokemon.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.theproject.browsepokemon.PokemonDisplayObject;
import org.theproject.browsepokemon.model.PokedexDAO;

import pokedex.PokemonSpecies;

public class PokedexServiceImpl implements PokedexService, Serializable {
    
    private static final long serialVersionUID = 2172441576698845339L;

    private static Logger logger = LoggerFactory.getLogger(PokedexServiceImpl.class);
    
    @Autowired
    private PokedexDAO pokedexDAO;

    public Map<Integer, String> getPokemonSpeciesNames(Set<java.lang.String> generations) {

        logger.info("In getPokemonSpeciesNames()...");

        List<PokemonSpecies> pokemonSpecies = pokedexDAO.getPokemonSpeciesForGenerations(generations);

        Map<Integer, String> result = new LinkedHashMap<Integer, String>();
        for (PokemonSpecies s : pokemonSpecies) {
            result.put(s.getId(), s.getIdentifier());
        }
        logger.info("Got " + result.keySet().size() + " pokemon names.");
        return result;
    }

}