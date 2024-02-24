package com.plucero.superhero.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String error, String message) {
}
