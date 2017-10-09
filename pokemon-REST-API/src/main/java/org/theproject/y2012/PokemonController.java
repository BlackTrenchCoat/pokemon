package org.theproject.y2012;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;
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

    @RequestMapping(value = "/pokemon/{name}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Pokemon getPokemon(@PathVariable(value = "name") String name) {
        Pokemon result = pokemonRepository.findByName(name);
        if (result == null) {
            throw new PokemonNotFoundException(name);
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

    private String getLocationForChildResource(HttpServletRequest request, Object childIdentifier) {
        StringBuffer url = request.getRequestURL();
        UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
        return template.expand(childIdentifier).toASCIIString();
    }
}
