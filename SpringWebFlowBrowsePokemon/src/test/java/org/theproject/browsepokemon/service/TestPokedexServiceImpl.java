package org.theproject.browsepokemon.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SystemTestConfig.class)
public class TestPokedexServiceImpl {
    
    @Autowired
    private PokedexService pokedexService;

    @Test
    public void testPokedexService() {
        Set<String> generations = new HashSet<String>();
        generations.add("generation-i");
        Map<Integer, String> speciesNames = pokedexService.getPokemonSpeciesNames(generations);
        assertNotNull(speciesNames);
    }

}
