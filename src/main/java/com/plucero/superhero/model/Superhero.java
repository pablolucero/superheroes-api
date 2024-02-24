package com.plucero.superhero.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public final class Superhero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private Superhero(String name) {
        this.name = name;
    }

    private Superhero(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Superhero named(String name) {
        return new Superhero(name);
    }

    public static Superhero named(Long id, String name) {
        return new Superhero(id, name);
    }
}
