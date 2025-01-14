package com.anikeeva.traineeship.workplacebooking.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserForUserDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserForUserDTO() {
        UserForUserDTO userDTO =
                new UserForUserDTO("John Doe", "1234567890",
                        "john.doe@example.com", false);

        Set<ConstraintViolation<UserForUserDTO>> violations = validator.validate(userDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void testInvalidEmail() {
        UserForUserDTO userDTO =
                new UserForUserDTO("John Doe", "1234567890",
                        "invalid-email", false);

        Set<ConstraintViolation<UserForUserDTO>> violations = validator.validate(userDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("должно иметь формат адреса электронной почты");
    }

    @Test
    void testBlankFullName() {
        UserForUserDTO userDTO = new UserForUserDTO("", "1234567890",
                "john.doe@example.com", false);

        Set<ConstraintViolation<UserForUserDTO>> violations = validator.validate(userDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("не должно быть пустым");
    }

    @Test
    void testNullPhoneNumber() {
        UserForUserDTO userDTO = new UserForUserDTO("John Doe", null,
                "john.doe@example.com", false);

        Set<ConstraintViolation<UserForUserDTO>> violations = validator.validate(userDTO);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("не должно равняться null");
    }

    @Test
    void testAllFieldsInvalid() {
        UserForUserDTO userDTO = new UserForUserDTO("", null,
                "invalid-email", false);

        Set<ConstraintViolation<UserForUserDTO>> violations = validator.validate(userDTO);

        assertThat(violations).hasSize(3);
    }
}
