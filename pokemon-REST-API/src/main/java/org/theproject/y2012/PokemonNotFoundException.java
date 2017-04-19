package org.theproject.y2012;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PokemonNotFoundException extends RuntimeException {
    
    PokemonNotFoundException(String name) {
        super("Pokemon named '" + name + "' was not found");
    }

}
