package org.theproject.browsepokemon.service;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.theproject.browsepokemon.model.PokedexDAO;

public class PokedexServiceImpl implements PokedexService, Serializable {
    
    private static final long serialVersionUID = 2172441576698845339L;

    private static Logger logger = LoggerFactory.getLogger(PokedexServiceImpl.class);
    
    @Autowired
    private PokedexDAO pokedexDAO;

    public Map<String, String> getPokemonGenerationNames() {
	    
	    logger.info("In getPokemonGenerationNames()...");
	    
        Map<String, String> result = pokedexDAO.getPokemonGenerationNames();
        return result;
    }

}