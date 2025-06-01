package com.thecommonroom.TheCommonRoom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Esta clase maneja excepciones de manera global para todos los controladores
public class GlobalExceptionHandler {

    // |=== EXCEPCIONES DE USUARIOS ===|
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Código de estado de la respuesta
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())); // Agrega el nombre del campo donde falló la validacion + su mensaje

        return errors;
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("username", ex.getMessage());
        return error;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleEmailAlreadyExists(EmailAlreadyExistsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("email", ex.getMessage());
        return error;
    }
}
