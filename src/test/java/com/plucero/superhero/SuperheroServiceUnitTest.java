package com.plucero.superhero;

import com.plucero.superhero.exception.SuperheroNotFoundException;
import com.plucero.superhero.model.Superhero;
import com.plucero.superhero.repository.SuperheroRepository;
import com.plucero.superhero.service.SuperheroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuperheroServiceUnitTest {

    @Mock
    private SuperheroRepository repository;

    @InjectMocks
    private SuperheroService superheroService;

    @Test
    void testCreateSuperhero() {
        Superhero superheroToSave = Superhero.named("Flash");
        Superhero savedSuperhero = Superhero.named(1L, "Flash");

        when(repository.save(superheroToSave)).thenReturn(savedSuperhero);

        Superhero createdSuperhero = superheroService.create(superheroToSave);

        assertNotNull(createdSuperhero.getId());
        assertEquals("Flash", createdSuperhero.getName());
    }

    @Test
    void testListSuperheroes() {
        List<Superhero> expectedSuperheroes = Arrays.asList(
                Superhero.named(1L, "Superman"),
                Superhero.named(2L, "Batman")
        );

        when(repository.findAll()).thenReturn(expectedSuperheroes);

        List<Superhero> actualSuperheroes = superheroService.list();

        assertEquals(expectedSuperheroes, actualSuperheroes);
    }

    @Test
    void testFindSuperheroById() {
        Superhero expectedSuperhero = Superhero.named(1L, "Spiderman");

        when(repository.findById(1L)).thenReturn(Optional.of(expectedSuperhero));

        Superhero actualSuperhero = superheroService.findById(1L);

        assertEquals(expectedSuperhero, actualSuperhero);
    }

    @Test
    void testFindSuperheroByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SuperheroNotFoundException.class, () -> superheroService.findById(1L));
    }

    @Test
    void testFindSuperheroesByName() {
        List<Superhero> expectedSuperheroes = Arrays.asList(
                Superhero.named(1L, "Superman"),
                Superhero.named(2L, "Batman")
        );

        when(repository.findByNameContainingIgnoreCase("man")).thenReturn(expectedSuperheroes);

        List<Superhero> actualSuperheroes = superheroService.findByName("man");

        assertEquals(expectedSuperheroes, actualSuperheroes);
    }

    @Test
    void testUpdateSuperhero() {
        Superhero superheroToUpdate = Superhero.named(1L, "Iron Man");

        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(superheroToUpdate)).thenReturn(superheroToUpdate);

        Superhero updatedSuperhero = superheroService.update(1L, superheroToUpdate);

        assertEquals(superheroToUpdate, updatedSuperhero);
    }

    @Test
    void testUpdateNonExistentSuperhero() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(SuperheroNotFoundException.class,
                () -> superheroService.update(1L, Superhero.named("Superman")));
    }

    @Test
    void testDeleteSuperhero() {
        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(superheroService.delete(1L));

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNonExistentSuperhero() {
        when(repository.existsById(1L)).thenReturn(false);

        assertFalse(superheroService.delete(1L));

        verify(repository, never()).deleteById(any());
    }
}
