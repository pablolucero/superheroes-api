package com.plucero.superhero;

import com.plucero.superhero.model.Superhero;

import java.util.List;

public class TestObjectBucket {
    public static List<Superhero> getSupermanBatmanAndFlash() {
        return List.of(
                Superhero.named(1L, "Superman"),
                Superhero.named(2L, "Batman"),
                Superhero.named(3L, "Flash")
        );
    }

    public static Superhero getSuperman() {
        return Superhero.named(1L, "Superman");
    }

    public static List<Superhero> getSupermanAndBatman() {
        return List.of(
                Superhero.named(1L, "Superman"),
                Superhero.named(2L, "Batman")
        );
    }
}
