package com.plucero.superhero.exception;

public class SuperheroAlreadyExistException extends RuntimeException {
    public SuperheroAlreadyExistException(String message) {
        super(message);
    }
}
