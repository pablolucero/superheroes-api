package com.plucero.superhero;

import com.plucero.superhero.controller.SuperheroController;
import com.plucero.superhero.model.Superhero;
import com.plucero.superhero.repository.SuperheroRepository;
import com.plucero.superhero.service.SuperheroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.plucero.superhero.TestObjectBucket.getSuperman;
import static com.plucero.superhero.TestObjectBucket.getSupermanAndBatman;
import static com.plucero.superhero.TestObjectBucket.getSupermanBatmanAndFlash;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SuperheroController.class)
@AutoConfigureMockMvc
class FunctionalEndpointWithMockedRepositoryTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuperheroRepository superheroRepository;

    @SpyBean
    private SuperheroService superheroService;

    @Test
    void listAllSuperheroes() throws Exception {

        when(superheroRepository.findAll()).thenReturn(getSupermanBatmanAndFlash());

        final String jsonResponse = """
                    [
                        {
                            "id": 1,
                            "name": "Superman"
                        },
                        {
                            "id": 2,
                            "name": "Batman"
                        },
                        {
                            "id": 3,
                            "name": "Flash"
                        }
                    ]
                """;

        mockMvc.perform(get("/api/superheroes"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void findById() throws Exception {

        when(superheroRepository.findById(1L)).thenReturn(Optional.of(getSuperman()));

        final String jsonResponse = """
                    {
                        "id": 1,
                        "name": "Superman"
                    }
                """;

        mockMvc.perform(get("/api/superheroes/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void findByIdNotFound() throws Exception {

        when(superheroRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/superheroes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchByName() throws Exception {

        final String SEARCHED_TERM = "man";

        when(superheroRepository.findByNameContainingIgnoreCase(SEARCHED_TERM))
                .thenReturn(getSupermanAndBatman());

        final String jsonResponse = """
                    [
                        {
                            "id": 1,
                            "name": "Superman"
                        },
                        {
                            "id": 2,
                            "name": "Batman"
                        }
                    ]
                """;

        mockMvc.perform(get("/api/superheroes/search?name=" + SEARCHED_TERM))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));

        verify(superheroRepository, times(1)).findByNameContainingIgnoreCase(SEARCHED_TERM);
    }

    @Test
    public void createSuperhero() throws Exception {
        final String jsonRequest = """
                    {
                      "name": "Deadpool"
                    }
                """;

        final String jsonResponse = """
                    {
                      "id": 1,
                      "name": "Deadpool"
                    }
                """;

        when(superheroRepository.save(Superhero.named("Deadpool")))
                .thenReturn(Superhero.named(1L, "Deadpool"));

        mockMvc.perform(post("/api/superheroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void modifySuperhero() throws Exception {
        when(superheroRepository.existsById(1L)).thenReturn(true);

        final Superhero supermanBizarro = Superhero.named(1L, "Superman bizarro");
        when(superheroRepository.save(supermanBizarro)).thenReturn(supermanBizarro);

        final String json = """
                    {
                      "id": 1,
                      "name": "Superman bizarro"
                    }
                """;

        mockMvc.perform(put("/api/superheroes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(superheroRepository, times(1)).save(supermanBizarro);
    }

    @Test
    public void modifySuperheroNotFound() throws Exception {
        when(superheroRepository.existsById(99L)).thenReturn(false);

        final String json = """
                    {
                      "id": 99,
                      "name": "Nonexistent superhero"
                    }
                """;

        mockMvc.perform(put("/api/superheroes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        verify(superheroRepository, times(0)).save(any());
    }

    @Test
    public void deleteSuperhero() throws Exception {
        when(superheroRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/superheroes/1"))
                .andExpect(status().isNoContent());

        verify(superheroRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteSuperheroNotFound() throws Exception {
        when(superheroRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/superheroes/99"))
                .andExpect(status().isNotFound());

        verify(superheroRepository, times(0)).deleteById(any());
    }

}
