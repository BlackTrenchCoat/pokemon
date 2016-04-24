package org.theproject.y2012.pokemon.model;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.theproject.y2012.pokemon.domain.Pokemon;

@Repository
@Transactional
public class PokemonDAOImpl implements PokemonDAO {

    public PokemonDAOImpl() {
    }
    
    private SessionFactory sessionFactory;

    @Autowired
    public PokemonDAOImpl(@Qualifier("pokemonSessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object o) {
        currentSession().save(o);
    }
    
    public List<Pokemon> getAllPokemon() {
        String queryString = "from Pokemon";
        Query query = currentSession().createQuery(queryString);
        List<Pokemon> result = query.list();
        return result;
    }
}
