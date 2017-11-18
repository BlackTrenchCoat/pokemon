package org.theproject.y2012;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.theproject.y2012.domain.Pokemon;

import java.util.List;

public interface PokemonRepository extends PagingAndSortingRepository<Pokemon, Long> {
    List<Pokemon> findAll();

    Pokemon findById(Long id);
    Pokemon findByName(String name);

    @Transactional
    Long deleteByName(String name);

    // JPA Named Query defined on the Pokemon entity
    List<Pokemon> findAllByType(@Param("type") String type);

    // Defining a (JPQL) query with @Query
    @Query("select p from Pokemon p where p.weight >= 500 and p.speed >= 100")
    List<Pokemon> findAllBigAndFast();

    // Native MySQL query
    @Query(value="select * from pokemon where color in ('red', 'white', 'blue')", nativeQuery=true)
    List<Pokemon> findAllAmerican();

    // More complex derived queries
    List<Pokemon> findAllByWeightBetween(int lo, int hi);

    List<Pokemon> findByColorAndNameLike(String color, String name);

}
