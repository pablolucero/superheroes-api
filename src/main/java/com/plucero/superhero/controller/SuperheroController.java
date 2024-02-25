package com.plucero.superhero.controller;

import com.plucero.superhero.model.Superhero;
import com.plucero.superhero.model.SuperheroRequest;
import com.plucero.superhero.service.SuperheroService;
import com.plucero.superhero.utils.LogExecutionTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    @Operation(summary = "List all the superheroes")
    @ApiResponse(responseCode = "200", description = "A superheroes list")
    public ResponseEntity<List<Superhero>> list() {
        return ResponseEntity.ok(superheroService.list());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    @Operation(summary = "Find a superhero by id")
    public ResponseEntity<Superhero> findById(@PathVariable Long id) {
        return ResponseEntity.ok(superheroService.findById(id));
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    @Operation(summary = "Find superheroes by name")
    public ResponseEntity<List<Superhero>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(superheroService.findByName(name));
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @LogExecutionTime
    @Operation(summary = "Creates a superhero")
    public Superhero create(@RequestBody @Valid SuperheroRequest superheroRequest) {
        Superhero superhero = Superhero.fromRequest(superheroRequest);
        return superheroService.create(superhero);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    @Operation(summary = "Updates a superhero")
    public ResponseEntity<Superhero> update(@PathVariable Long id, @RequestBody Superhero superhero) {
        return ResponseEntity.ok(superheroService.update(id, superhero));
    }

    @DeleteMapping(path = "/{id}")
    @LogExecutionTime
    @Operation(summary = "Deletes a superhero")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (superheroService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
