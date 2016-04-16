package org.theproject.browsepokemon.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.theproject.browsepokemon.PokemonDisplayObject;

import pokedex.PokemonSpecies;

@Repository
@Transactional(readOnly=true)
public class PokedexDAOImpl implements PokedexDAO {

    private static Logger logger = LoggerFactory.getLogger(PokedexDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

//    public PokedexDAOImpl() {
//    }
//
//    @Autowired
//    public PokedexDAOImpl(SessionFactory sessionFactory) {
//        logger.info("*** In the PokedexDAOImpl constructor.  sessionFactory = " + sessionFactory);
//        this.sessionFactory = sessionFactory;
//    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    // Application methods
    
    private static final String GENERATION_NAMES_QUERY =
            "select g.identifier, vn.name "
                    + "from Generations g, VersionNames vn, Languages l, VersionGroups vg, Versions v "
                    + "where g.id = vg.generations.id " 
                    + "and vg.id = v.versionGroups.id "
                    + "and v.id = vn.versions.id " 
                    + "and l.id = vn.languages.id "
                    + "and l.identifier = 'en'";

    public Map<String, String> getPokemonGenerationNames() {
        
        logger.info("In getPokemonGenerationNames()...");
        
    	Map<String, String> result = new TreeMap<String, String>();
    	Query query = currentSession().createQuery(GENERATION_NAMES_QUERY);
    	List<Object[]> rowList = query.list();
        for (Object[] row : rowList) {
        	String generation = (String)row[0];
        	String version = (String)row[1];
            if (result.get(generation) == null) {
                result.put(generation, version);
            } else {
                result.put(generation, result.get(generation) + ", " + version);
            }
        }
    	return result;
    }
    
    private static final String SPECIES_QUERY = 
            "select ps " 
                    + "from PokemonSpecies ps, Generations g " 
                    + "where ps.generations.id = g.id " 
                    + "and g.identifier in :generations " 
                    + "order by ps.identifier";

    public List<PokemonSpecies> getPokemonSpeciesForGenerations(
            Set<java.lang.String> generations) {
        Query query = currentSession()
                .createQuery(SPECIES_QUERY)
                .setParameterList("generations", generations);
        List<PokemonSpecies> result = (List<PokemonSpecies>)query.list();
        return result;
    }

    private static final String DISPLAY_QUERY =
            "select p.height, p.weight, p.baseExperience, "
                    + "species.identifier, species.baseHappiness, "
                    + "pc.identifier, shapes.identifier "
                    + "from Pokemon p, PokemonSpecies species, PokemonColors pc, "
                    + "PokemonShapes shapes "
                    + "where p.pokemonSpecies.id = :id "
                    + "and p.pokemonSpecies.id = species.id "
                    + "and species.pokemonColors.id = pc.id "
                    + "and species.pokemonShapes.id = shapes.id";
    
    private static final String HABITAT_QUERY =
            "select ph.identifier "
                    + "from PokemonSpecies species, PokemonHabitats ph "
                    + "where species.id = :id "
                    + "and species.pokemonHabitats.id = ph.id";
    
    public PokemonDisplayObject getDisplayObject(Integer id) {
        Object[] row = (Object[])currentSession()
                .createQuery(DISPLAY_QUERY)
                .setParameter("id", id)
                .setMaxResults(1)
                .uniqueResult();
        PokemonDisplayObject result = new PokemonDisplayObject(
                (String)row[3], // name
                (Integer)row[0], // height
                (Integer)row[1], // weight
                (Integer)row[2], // base experience
                (Integer)row[4], // base happiness
                (String)row[5], // color
                (String)row[6] // shape
                );
        // Special handling for habitat because pokemon_species.habitat_id = null
        // for some Black/White Pokemon
        String habitat = (String)currentSession()
                .createQuery(HABITAT_QUERY)
                .setParameter("id", id)
                .setMaxResults(1)
                .uniqueResult();
        if (habitat != null) {
            result.setHabitat(habitat);
        }
        // Get stats
        getStats(id, result);        
        return result;
    }

    private static final String STATS_QUERY = 
            "select s.identifier, ps.baseStat "
                    + "from PokemonStats ps, Stats s " 
                    + "where ps.id.pokemonId = :id "
                    + "and ps.id.statId = s.id";

    private void getStats(Integer id, PokemonDisplayObject result) {
        Query query = currentSession().createQuery(STATS_QUERY).setParameter("id", id);
        List<Object[]> rowList = query.list();
        for (Object[] row : rowList) {
            String name = (String) row[0];
            Integer baseStat = (Integer) row[1];
            if (name.equals("hp")) {
                result.setHp(baseStat);
            } else if (name.equals("attack")) {
                result.setAttack(baseStat);
            } else if (name.equals("defense")) {
                result.setDefense(baseStat);
            } else if (name.equals("special-attack")) {
                result.setSpecialAttack(baseStat);
            } else if (name.equals("special-defense")) {
                result.setSpecialDefense(baseStat);
            } else if (name.equals("speed")) {
                result.setSpeed(baseStat);
            }
        }
    }
    
    
    
}

