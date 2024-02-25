package com.plucero.superhero.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record SuperheroRequest(@NotNull @Valid String name) {
}
