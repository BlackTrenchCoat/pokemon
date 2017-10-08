package org.theproject.y2012.pokemonCassandra;

import org.springframework.data.repository.CrudRepository;
import org.theproject.y2012.pokemonCassandra.cassandraDomain.CassandraPokemonEntity;

public interface CassandraPokemonEntityRepository extends CrudRepository<CassandraPokemonEntity, String> {
}
