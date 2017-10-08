package org.theproject.y2012.pokemonCassandra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.theproject.y2012.pokemonCassandra.mysqlDomain.MysqlPokemonEntity;

import java.util.List;

public interface MysqlPokemonEntityRepository extends JpaRepository<MysqlPokemonEntity, Long> {
    List<MysqlPokemonEntity> findAll();

/*
    MysqlPokemonEntity findByName(String name);

    @Transactional
    Long deleteByName(String name);
*/
}
