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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class PokemonSpringDataRestApiApplicationTests {
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    PokemonRepository pokemonRepository;
    
    private List<Pokemon> pokemonList = new ArrayList<Pokemon>();

    private static final String pokemonName = "Mewtwo";
    
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

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
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.pokemonRepository.deleteAllInBatch();
        this.pokemonList.add(pokemonRepository.save(
                new Pokemon(150, pokemonName, 20, 1220,154, 90,
                        106, 106,130, 5, "purple", 0, 0,
                        null, 20,"Genetic", "psychic", null,1220)));
    }
    
    @Test
    public void pokemonNotFound() throws Exception {
        mockMvc.perform(get("/pokemon/Snorlax"))
        .andExpect(status().isNotFound());
    }

    @Test
    public void readPokemonById() throws Exception {
        String pokemonIdString = this.pokemonList.get(0).getId().toString();
        mockMvc.perform(get("/pokemons/" + pokemonIdString))
        .andExpect(status().isOk())
        .andExpect(content().contentType(jsonContentType))
        .andExpect(jsonPath("$.type1", is("psychic")))
        .andExpect(jsonPath("$.height", is(20)))
        .andExpect(jsonPath("$.weight", is(1220)));
    }
    
    @Test
    public void readPokemonByName() throws Exception {
        mockMvc.perform(get("/pokemons/search/findByName?name=" + pokemonName))
        .andExpect(status().isOk())
        .andExpect(content().contentType(jsonContentType))
        .andExpect(jsonPath("$.type1", is("psychic")))
        .andExpect(jsonPath("$.height", is(20)))
        .andExpect(jsonPath("$.weight", is(1220)));
    }
    
    @Test
    public void readPokemons() throws Exception {
        mockMvc.perform(get("/pokemons"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(jsonContentType))
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content[0].type1", is("psychic")))
        .andExpect(jsonPath("$.content[0].height", is(20)))
        .andExpect(jsonPath("$.content[0].weight", is(1220)));
    }
    
    @Test
    public void createPokemon() throws Exception {
        String pokemonJson = json(new Pokemon(
                96, "Drowzee", 48, 45, 43, 90,
                60, 70, 42, 5, "yellow", 102, 70, "grassland",
                10, "", "psychic", null, 324));
        mockMvc.perform(post("/pokemons")
                .contentType(jsonContentType)
                .content(pokemonJson))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void updatePokemon() throws Exception {
        String pokemonJson = json(
                new Pokemon(150, pokemonName, 20, 1220,154, 90,
                        106, 106,130, 5, "purple", 0, 0,
                        null, 20,"Genetic", "psychic", null,1220));
        String pokemonIdString = this.pokemonList.get(0).getId().toString();
        mockMvc.perform(put("/pokemons/" + pokemonIdString)
        .contentType(jsonContentType)
        .content(pokemonJson))
        .andExpect(status().isNoContent());
    }
    
    @Test
    public void deletePokemon() throws Exception {
        String pokemonIdString = this.pokemonList.get(0).getId().toString();
        mockMvc.perform(delete("/pokemons/" + pokemonIdString))
        .andExpect(status().isNoContent());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
    
}
