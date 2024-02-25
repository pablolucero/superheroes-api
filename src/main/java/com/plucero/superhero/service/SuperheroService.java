package com.plucero.superhero.service;

import com.plucero.superhero.exception.SuperheroAlreadyExistException;
import com.plucero.superhero.exception.SuperheroNotFoundException;
import com.plucero.superhero.model.Superhero;
import com.plucero.superhero.repository.SuperheroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SuperheroService {

    private final SuperheroRepository repository;

    public SuperheroService(SuperheroRepository repository) {
        this.repository = repository;
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Cacheable("superheroes")
    public List<Superhero> list() {
        return repository.findAll();
    }

    public Superhero findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new SuperheroNotFoundException("Superhero not found with the selected id"));
    }

    @Cacheable("superheroes")
    public List<Superhero> findByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @CacheEvict(value = "superheroes", allEntries = true)
    public Superhero create(Superhero superhero) {
        if (repository.existsByNameIgnoreCase(superhero.getName())) {
            throw new SuperheroAlreadyExistException("Superhero already exist with the same name");
        }
        return repository.save(superhero);
    }

    @CacheEvict(value = "superheroes", allEntries = true)
    public Superhero update(Long id, Superhero superhero) {
        if (!repository.existsById(id)) {
            throw new SuperheroNotFoundException("Superhero not found with the selected id");
        }
        superhero.setId(id);
        return repository.save(superhero);
    }
}
