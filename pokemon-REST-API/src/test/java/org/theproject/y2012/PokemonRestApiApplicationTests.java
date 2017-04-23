package org.theproject.y2012;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.theproject.y2012.domain.Pokemon;

// See https://spring.io/guides/tutorials/bookmarks/

// Some integration tests for the RESTful endpoints exposed in PokemonController

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PokemonRestApiApplication.class)
@ActiveProfiles(profiles = "test")
public class PokemonRestApiApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    PokemonRepository pokemonRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);
        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }    

    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<Pokemon> pokemonList = new ArrayList<Pokemon>();

    private static final String pokemonName = "Mewtwo";
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.pokemonRepository.deleteAllInBatch();
        this.pokemonList.add(pokemonRepository.save(
                new Pokemon(150, pokemonName, 20, 1220, "psychic", null,
                        "Genetic", 0, "purple", null, 110, 90, 154, 90,
                        130, 106, 106, 0, 5)));
    }
    
    @Test
    public void pokemonNotFound() throws Exception {
        mockMvc.perform(get("/pokemon/Snorlax"))
        .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void readPokemon() throws Exception {
        mockMvc.perform(get("/pokemon/" + pokemonName))
        .andExpect(status().isOk())
        .andExpect(content().contentType(jsonContentType))
        .andExpect(jsonPath("$.id", is(this.pokemonList.get(0).getId().intValue())))
        .andExpect(jsonPath("$.type1", is("psychic")))
        .andExpect(jsonPath("$.height", is(20)))
        .andExpect(jsonPath("$.weight", is(1220)));
    }
    
    @Test
    public void readPokemons() throws Exception {
        mockMvc.perform(get("/pokemons"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(jsonContentType))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(this.pokemonList.get(0).getId().intValue())))
        .andExpect(jsonPath("$[0].type1", is("psychic")))
        .andExpect(jsonPath("$[0].height", is(20)))
        .andExpect(jsonPath("$[0].weight", is(1220)));
    }
    
    @Test
    public void createPokemon() throws Exception {
        String pokemonJson = json(new Pokemon(96, "Drowzee", 10, 324, "psychic", null,
                "Hypnosis", 70, "yellow", null, 48, 45, 43, 90,
                42, 60, 60, 0, 5));
        mockMvc.perform(post("/pokemons")
                .contentType(jsonContentType)
                .content(pokemonJson))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void updatePokemon() throws Exception {
        String pokemonJson = json(new Pokemon(150, pokemonName, 20, 1220, "psychic", null, "Genetic", 0, "beige",
                null, 110, 90, 154, 90, 130, 106, 106, 0, 5));
        mockMvc.perform(put("/pokemon/" + pokemonName)
        .contentType(jsonContentType)
        .content(pokemonJson))
        .andExpect(status().isNoContent());
    }
    
    @Test
    public void deletePokemon() throws Exception {
        mockMvc.perform(delete("/pokemon/" + pokemonName))
        .andExpect(status().isNoContent());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
    
}
