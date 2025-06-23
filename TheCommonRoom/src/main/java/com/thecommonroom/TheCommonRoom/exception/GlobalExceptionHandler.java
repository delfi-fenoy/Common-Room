package com.thecommonroom.TheCommonRoom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleEmailAlreadyExists(EmailAlreadyExistsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("email", ex.getMessage());
        return error;
    }

    @ExceptionHandler(NoUsersFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Código 404
    public Map<String, String> handleNoUsersFound(NoUsersFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUsernameNotFound(UsernameNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("username", ex.getMessage());
        return error;
    }

    // |=== EXCEPCIONES DE API ===|

    // Errores del cliente (4**)
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handleHttpClientError(HttpClientErrorException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Client error");
        error.put("message", ex.getStatusText());
        return ResponseEntity
                .status(ex.getStatusCode()) // Código de error
                .body(error); // Mensaje
    }

    // Para cuando se quiere hacer una reseña de una película que no existe (por su id)
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMovieNotFound(MovieNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    // Errores del servidor (5**)
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<Map<String, String>> handleHttpServerError(HttpServerErrorException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Server error");
        error.put("message", ex.getStatusText());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(error);
    }

    // |=== EXCEPCIONES DE REVIEWS ===|

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleReviewAlreadyExists(ReviewAlreadyExistsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReviewNotFound(ReviewNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InvalidReviewException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRating(InvalidReviewException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    // |=== EXCEPCIONES DE ENTRADA DE DATOS ===|

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid parameter");
        error.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(PageOutOfBoundsException.class)
    public ResponseEntity<Map<String, String>> handlePageOutOfBounds(PageOutOfBoundsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid page number");
        error.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    // |=== EXCEPCIONES DE JWT ===|

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid request");
        error.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid username or password");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // La autenticación falló por username o password
                .body(error);
    }

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<Map<String, String>> handleAccessDenied(Exception ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", "Access denied");
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN) // El usuario ya está autenticado, pero no tiene permiso para acceder al recurso
                .body(error);
    }

}
