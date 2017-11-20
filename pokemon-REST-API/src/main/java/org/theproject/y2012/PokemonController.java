package org.theproject.y2012;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;
import org.theproject.y2012.domain.Move;
import org.theproject.y2012.domain.Pokemon;

@RestController
public class PokemonController {

    @Autowired
    PokemonRepository pokemonRepository;

    @RequestMapping(value = "/pokemons", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Pokemon> listPokemons() {
        return pokemonRepository.findAll();
    }

    // JPA Named Query
    @RequestMapping(value = "/pokemonsByType/{type}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Pokemon> listPokemonsByType(@PathVariable(value = "type") String type) {
        return pokemonRepository.findAllByType(type);
    }

    // Query defined with @Query
    @RequestMapping(value = "/bigAndFastPokemons", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Pokemon> listBigAndFastPokemons() {
        return pokemonRepository.findAllBigAndFast();
    }

    // Native MySQL query defined with @Query
    @RequestMapping(value="/allAmericanPokemons", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Pokemon> listAllAmericanPokemons() {
        return pokemonRepository.findAllAmerican();
    }

    // Paging
    @RequestMapping(value="/pagedPokemons/{pageNumber}/{pageSize}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Pokemon> pagedPokemons(@PathVariable(value = "pageNumber") int pageNumber,
            @PathVariable(value = "pageSize") int pageSize) {
        Page<Pokemon> pokemons = pokemonRepository.findAll(new PageRequest(pageNumber, pageSize));
        return pokemons.getContent();
    }

    // Paging and sorting
    @RequestMapping(value="/pagedSortedPokemons/{pageNumber}/{pageSize}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Pokemon> pagedSortedPokemons(@PathVariable(value = "pageNumber") int pageNumber,
            @PathVariable(value = "pageSize") int pageSize) {
        Page<Pokemon> pokemons = pokemonRepository.findAll(new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "name"));
        return pokemons.getContent();
    }

    @RequestMapping(value = "/pokemonByName/{name}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Pokemon getPokemonByName(@PathVariable(value = "name") String name) {
        Pokemon result = pokemonRepository.findByName(name);
        if (result == null) {
            throw new PokemonNotFoundException(name);
        }
        return result;
    }

    @RequestMapping(value="/pokemonById/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Pokemon getPokemonById(@PathVariable(value = "id") Long id) {
        Pokemon result = pokemonRepository.findById(id);
        if (result == null) {
            throw new PokemonNotFoundException(id);
        }
        return result;

    }

    @RequestMapping(value = "/pokemons", consumes = "application/json", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createPokemon(@RequestBody Pokemon newPokemon, HttpServletRequest request,
            HttpServletResponse response) {
        pokemonRepository.save(newPokemon);
        response.addHeader("Location", getLocationForChildResource(request, newPokemon.getId()));
    }

    @RequestMapping(value = "/pokemon/{name}", consumes = "application/json", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePokemon(@PathVariable(value = "name") String name, @RequestBody Pokemon newPokemon,
            HttpServletRequest request, HttpServletResponse response) {
        Pokemon oldPokemon = pokemonRepository.findByName(name);
        if (oldPokemon == null) {
            throw new PokemonNotFoundException(name);
        } else {
            newPokemon.setId(oldPokemon.getId());
            pokemonRepository.save(newPokemon);
            response.addHeader("Location", getLocationForChildResource(request, newPokemon.getId()));
        }
    }

    @RequestMapping(value = "/pokemon/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePokemon(@PathVariable(value = "name") String name) {
        Long count = pokemonRepository.deleteByName(name);
        if (count == 0) {
            throw new PokemonNotFoundException(name);
        }
    }

    @RequestMapping(value = "/mediumWeightPokemons/{lo}/{hi}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Pokemon> listMediumWeightPokemons(@PathVariable(value="lo") int lo,
            @PathVariable(value="hi") int hi) {
        return pokemonRepository.findAllByWeightBetween(lo, hi);
    }

    @RequestMapping(value = "/pokemonWithColorWhoseNameMatches/{color}/{name}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Pokemon> pokemonWithColorWhoseNameMatches(@PathVariable(value="color") String color,
            @PathVariable(value="name") String name) {
        return pokemonRepository.findByColorAndNameLike(color, name);
    }

    @RequestMapping(value = "/listMovesOfPokemonByName/{name}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Set<Move> listMovesOfPokemonByName(@PathVariable(value="name") String name) {
        Pokemon pokemon = pokemonRepository.findByName(name);
        Set<Move> moves = pokemon.getMoves();
        return moves;
    }

    private String getLocationForChildResource(HttpServletRequest request, Object childIdentifier) {
        StringBuffer url = request.getRequestURL();
        UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
        return template.expand(childIdentifier).toASCIIString();
    }
}
