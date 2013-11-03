package org.theproject.browsepokemon.model;

import java.io.Serializable;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pokedex.PokemonSpecies;

@Repository
@Transactional
public class PokedexDAOImpl implements PokedexDAO, Serializable {

    private static final long serialVersionUID = -5097470718065288080L;

    private static transient Logger logger = LoggerFactory.getLogger(PokedexDAOImpl.class);

    private transient SessionFactory sessionFactory;

    public PokedexDAOImpl() {
    }

    @Autowired
    public PokedexDAOImpl(@Qualifier("pokedexSessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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
    

}

