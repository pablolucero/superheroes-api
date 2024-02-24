package com.plucero.superhero.controller;

import com.plucero.superhero.model.Superhero;
import com.plucero.superhero.service.SuperheroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/superheroes")
public class SuperheroController {

    private final SuperheroService superheroService;

    public SuperheroController(SuperheroService superheroService) {
        this.superheroService = superheroService;
    }

    @GetMapping("")
    public ResponseEntity<List<Superhero>> list() {
        return ResponseEntity.ok(superheroService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Superhero> findById(@PathVariable Long id) {
        return ResponseEntity.ok(superheroService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Superhero>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(superheroService.findByName(name));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Superhero create(@RequestBody @Valid Superhero superhero) {
        return superheroService.create(superhero);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Superhero> update(@PathVariable Long id, @RequestBody Superhero superhero) {
        return ResponseEntity.ok(superheroService.update(id, superhero));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (superheroService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
