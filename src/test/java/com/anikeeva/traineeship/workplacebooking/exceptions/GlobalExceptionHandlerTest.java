package com.anikeeva.traineeship.workplacebooking.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.AccessDeniedException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {GlobalExceptionHandler.class})
@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleAuthenticationException() {
        ResponseEntity<String> actualHandleAuthenticationExceptionResult = globalExceptionHandler
                .handleAuthenticationException(new AccountExpiredException("Msg"));

        HttpStatusCode statusCode = actualHandleAuthenticationExceptionResult.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        assertEquals("Msg", actualHandleAuthenticationExceptionResult.getBody());
        assertEquals(401, actualHandleAuthenticationExceptionResult.getStatusCode().value());
        assertEquals(HttpStatus.UNAUTHORIZED, statusCode);
        assertTrue(actualHandleAuthenticationExceptionResult.hasBody());
        assertTrue(actualHandleAuthenticationExceptionResult.getHeaders().isEmpty());
    }

    @Test
    void testHandleAccessDeniedException() {
        ResponseEntity<String> actualHandleAccessDeniedExceptionResult = globalExceptionHandler
                .handleAccessDeniedException(new AccessDeniedException("foo"));

        HttpStatusCode statusCode = actualHandleAccessDeniedExceptionResult.getStatusCode();
        assertInstanceOf(HttpStatus.class, statusCode);
        assertEquals("foo", actualHandleAccessDeniedExceptionResult.getBody());
        assertEquals(403, actualHandleAccessDeniedExceptionResult.getStatusCode().value());
        assertEquals(HttpStatus.FORBIDDEN, statusCode);
        assertTrue(actualHandleAccessDeniedExceptionResult.hasBody());
        assertTrue(actualHandleAccessDeniedExceptionResult.getHeaders().isEmpty());
    }
}
