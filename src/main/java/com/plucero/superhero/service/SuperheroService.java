package com.plucero.superhero.service;

import com.plucero.superhero.exception.SuperheroNotFoundException;
import com.plucero.superhero.model.Superhero;
import com.plucero.superhero.repository.SuperheroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public List<Superhero> list() {
        return repository.findAll();
    }

    public Superhero findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new SuperheroNotFoundException("Superhero not found with the selected id"));
    }

    public List<Superhero> findByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public Superhero create(Superhero superhero) {
        return repository.save(superhero);
    }

    public Superhero update(Long id, Superhero superhero) {
        if (!repository.existsById(id)) {
            throw new SuperheroNotFoundException("Superhero not found with the selected id");
        }
        superhero.setId(id);
        return repository.save(superhero);
    }
}
