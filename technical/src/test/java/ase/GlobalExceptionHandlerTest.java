package ase;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleIllegalArgument() {
        ResponseEntity<String> response = handler.handleIllegalArgument(new IllegalArgumentException("test"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("test", response.getBody());
    }

    @Test
    void handleIllegalState() {
        ResponseEntity<String> response = handler.handleIllegalState(new IllegalStateException("test"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("test", response.getBody());
    }
}