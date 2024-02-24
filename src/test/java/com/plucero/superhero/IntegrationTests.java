package com.plucero.superhero;

import com.plucero.superhero.model.Superhero;
import com.plucero.superhero.service.SuperheroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTests {

    @Autowired
    private SuperheroService superheroService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void listAllSuperheroes() throws Exception {
        // 3 superheroes were inserted in the db after running the migrations
        assertThat(superheroService.list()).hasSize(3);

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
        assertThat(superheroService.list()).hasSize(3);

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

        mockMvc.perform(get("/api/superheroes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void searchByName() throws Exception {

        final String SEARCHED_TERM = "man";

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
                      "id": 4,
                      "name": "Deadpool"
                    }
                """;

        mockMvc.perform(post("/api/superheroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void modifySuperhero() throws Exception {

        final Superhero supermanBizarro = Superhero.named(1L, "Superman bizarro");

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
    }

    @Test
    public void modifySuperheroNotFound() throws Exception {

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

    }

    @Test
    public void deleteSuperhero() throws Exception {

        mockMvc.perform(delete("/api/superheroes/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/superheroes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteSuperheroNotFound() throws Exception {

        mockMvc.perform(delete("/api/superheroes/99"))
                .andExpect(status().isNotFound());

    }
}
