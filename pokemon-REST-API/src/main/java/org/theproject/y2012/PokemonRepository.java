package org.theproject.y2012;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.theproject.y2012.domain.Pokemon;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    List<Pokemon> findAll();

    Pokemon findByName(String name);

    @Transactional
    Long deleteByName(String name);
}
