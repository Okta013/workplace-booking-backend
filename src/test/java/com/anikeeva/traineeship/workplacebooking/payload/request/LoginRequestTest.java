package com.anikeeva.traineeship.workplacebooking.payload.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testLoginRequest_ValidInput_ShouldPassValidation() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testLoginRequest_NullEmail_ShouldFailValidation() {
        LoginRequest loginRequest = new LoginRequest(null, "password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertEquals(1, violations.size());
        ConstraintViolation<LoginRequest> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("не должно равняться null", violation.getMessage());
    }

    @Test
    void testLoginRequest_NullPassword_ShouldFailValidation() {
        LoginRequest loginRequest = new LoginRequest("user@example.com", null);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertEquals(1, violations.size());
        ConstraintViolation<LoginRequest> violation = violations.iterator().next();
        assertEquals("password", violation.getPropertyPath().toString());
        assertEquals("не должно равняться null", violation.getMessage());
    }

    @Test
    void testLoginRequest_NullEmailAndPassword_ShouldFailValidation() {
        LoginRequest loginRequest = new LoginRequest(null, null);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        assertEquals(2, violations.size());
    }
}
