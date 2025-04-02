package org.example.exceptions.handlers;

import com.auth0.jwk.JwkException;
import io.jsonwebtoken.JwtException;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.RepositoryException;
import org.example.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Entity not found: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Bad request: " + e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Illegal state: " + e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(JwtException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Illegal JWT: " + e.getMessage());
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<?> handleRepositoryException(RepositoryException e){
        if(e.getCause() instanceof IllegalArgumentException ex){
            return handleIllegalArgumentException(ex);
        }
        else if(e.getCause() instanceof NotFoundException ex){
            return handleNotFoundException(ex);
        }
        else if(e.getCause() instanceof IllegalStateException ex){
            return handleIllegalStateException(ex);
        }
        else if(e.getCause() instanceof RepositoryException){
            return handleRepositoryException(e);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Repository exception: " + e.getMessage());
        }

    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleServiceException(ServiceException e){

        if(e.getCause() instanceof IllegalArgumentException ex){
            return handleIllegalArgumentException(ex);
        }
        else if(e.getCause() instanceof NotFoundException ex){
            return handleNotFoundException(ex);
        }
        else if(e.getCause() instanceof IllegalStateException ex){
            return handleIllegalStateException(ex);
        }
        else if(e.getCause() instanceof RepositoryException ex){
            return handleRepositoryException(ex);
        }
        else if(e.getCause() instanceof ServiceException ex){
            return handleServiceException(ex);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Service exception: " + e.getMessage());
        }
    }



}
