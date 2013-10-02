package org.the_project.y2012.pokemon.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.the_project.y2012.pokemon.domain.Move;

import pokedex.Pokemon;
import pokedex.PokemonSpecies;

@Repository
@Transactional
public class PokedexDAOImpl implements PokedexDAO {
    private static Logger logger = Logger.getLogger("PokedexDAO");
    
    private SessionFactory sessionFactory;
    
    public PokedexDAOImpl() {
    }

    @Autowired
    public PokedexDAOImpl(@Qualifier("pokedexSessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public List<PokemonSpecies> getGen1AndGen2Pokemon() {
        List<PokemonSpecies> result = new LinkedList<PokemonSpecies>();
        // Generation 1 = red, blue, yellow
        // Generation 2 = gold, silver
        
        // Get PokemonSpecies objects
        
        Criteria criteria = currentSession().createCriteria(pokedex.PokemonSpecies.class).add(
                Restrictions.or(Restrictions.eq("generationId", new Integer(1)),
                        Restrictions.eq("generationId", new Integer(2))));
        result = (List<PokemonSpecies>)criteria.list();

//        Criteria criteria = currentSession().createCriteria(pokedex.Pokemon.class).add(
//                Restrictions.or(Restrictions.eq("generationId", new Integer(1)),
//                        Restrictions.eq("generationId", new Integer(2))));

        // Query query = session.createQuery("from Pokemon");
//        result = (List<Pokemon>)criteria.list();
        logger.info(result.size() + " pokemon in the pokedex.");
        return result;
    }

    private static final String typesQuery = "select t.identifier, pt.id.slot "
            + "from PokemonTypes pt, Types t " + "where pt.id.pokemonId = :id "
            + "and pt.types.id = t.id";

    public String[] getTypes(int id) {
        String[] result = null;
        Query query = currentSession().createQuery(typesQuery).setParameter("id", id);
        List<Object[]> rowList = query.list();
        result = new String[2];
        for (Object[] row : rowList) {
            String name = (String) row[0];
            Integer slot = (Integer) row[1];
            result[slot - 1] = name;
        }
        return result;
    }

    private static final String statsQuery = "select s.identifier, ps.baseStat "
            + "from PokemonStats ps, Stats s " + "where ps.id.pokemonId = :id "
            + "and ps.id.statId = s.id";

    public Map<String, Integer> getStats(int id) {
        HashMap<String, Integer> result = null;
        Query query = currentSession().createQuery(statsQuery).setParameter("id", id);
        List<Object[]> rowList = query.list();
        result = new HashMap<String, Integer>();
        for (Object[] row : rowList) {
            String name = (String) row[0];
            Integer baseStat = (Integer) row[1];
            result.put(name, baseStat);
        }
        return result;
    }

    private static final String movesQuery = "select distinct m.id, m.identifier, t.identifier, pm.id.level, m.power, m.pp, "
            + "m.accuracy, m.priority, mt.identifier, mdc.identifier, mep.shortEffect, "
            + "m.effectChance "
            + "from Moves m, PokemonMoves pm, Types t, MoveTargets mt, "
            + "MoveDamageClasses mdc, MoveEffectProse mep, Versions v, "
            + "PokemonMoveMethods pmm "
            + "where pm.id.pokemonId = :id "
            + "and pm.id.level <= :level "
            + "and v.identifier in ('gold', 'silver') "   // TODO: Huh?
            + "and pmm.identifier = 'level-up' "
            + "and pm.id.versionGroupId = v.versionGroups.id "
            + "and pm.id.moveId = m.id "
            + "and pm.id.pokemonMoveMethodId = pmm.id "
            + "and m.moveTargets.id = mt.id "
            + "and m.moveDamageClasses.id = mdc.id "
            + "and m.moveEffects.id = mep.id.moveEffectId "
            + "and m.types.id = t.id "
            + "order by pm.id.level";

    public Set<Move> findPossibleMoves(int pokemonPokedexId, int level) {
        Set<Move> result = null;
        Query query = currentSession().createQuery(movesQuery).setParameter("id", pokemonPokedexId)
                .setParameter("level", level);
        List<Object[]> rowList = query.list();
        result = new HashSet<Move>();
        for (Object[] row : rowList) {
            int movePokedexId = ((Integer) row[0]).intValue();
            String name = (String) row[1];
            String type = (String) row[2];
            int moveLevel = ((Integer) row[3]).intValue();
            int power = ((Integer) row[4]).intValue();
            int pp = ((Integer) row[5]).intValue();
            int accuracy = row[6] == null ? 0 : ((Integer) row[6]).intValue();
            int priority = ((Integer) row[7]).intValue();
            String target = (String) row[8];
            String damageClass = (String) row[9];
            String effect = (String) row[10];
            int effectChance = row[11] == null ? 0 : ((Integer) row[11]).intValue();
            Move move = new Move(movePokedexId, name, type, moveLevel, power, pp,
                    accuracy, priority, target, damageClass, effect,
                    effectChance);
            result.add(move);
        }
        return result;
    }
    
    // Get species and name
    private static final String getSpeciesNameQuery = 
            "select genus, name "
                    + "from PokemonSpeciesNames "
                    + "where id.localLanguageId = 9 "
                    + "and id.pokemonSpeciesId = :id";
    
    public SpeciesName getSpeciesName(int id) {
        Object[] row = (Object[])currentSession()
                .createQuery(getSpeciesNameQuery)
                .setParameter("id", id)
                .setMaxResults(1)
                .uniqueResult();
        return new SpeciesName((String)row[0], (String)row[1]);
    }
    
    private static final String getNameQuery = 
            "select ps.identifier "
                    + "from Pokemon p, PokemonSpecies ps "
                    + "where p.pokemonSpecies.id = ps.id "
                    + "and p.pokemonSpecies.id = :id";

    public String getName(Pokemon pokemon) {
        String result = (String)currentSession()
                .createQuery(getNameQuery)
                .setParameter("id", pokemon.getPokemonSpecies().getId())
                .setMaxResults(1)
                .uniqueResult();
        return result;
    }
    
    private static final String getHeightWeightQuery =
            "select height, weight "
                    + "from Pokemon "
                    + "where pokemonSpecies.id = :id";
    
    public HeightWeight getHeightWeight(int id) {
        Object[] row = (Object[])currentSession()
                .createQuery(getHeightWeightQuery)
                .setParameter("id", id)
                .setMaxResults(1)
                .uniqueResult();
        return new HeightWeight((Integer)row[0], (Integer)row[1]);
    }
    
    private static final String getColorQuery = 
            "select pc.identifier "
                    + "from PokemonSpecies ps, PokemonColors pc "
                    + "where ps.pokemonColors.id = pc.id "
                    + "and ps.id = :id";

    public String getColor(int id) {
        String result = (String)currentSession()
                .createQuery(getColorQuery)
                .setParameter("id", id)
                .setMaxResults(1)
                .uniqueResult();
        return result;
    }
}
