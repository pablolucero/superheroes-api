package com.plucero.superhero.repository;

import com.plucero.superhero.model.Superhero;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperheroRepository extends ListCrudRepository<Superhero, Long> {
    List<Superhero> findByNameContainingIgnoreCase(String searchTerm);
}
