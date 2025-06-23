package com.thecommonroom.TheCommonRoom.exception;

public class MovieListNotFoundException extends RuntimeException {
    public MovieListNotFoundException(String message) {
        super(message);
    }
}
