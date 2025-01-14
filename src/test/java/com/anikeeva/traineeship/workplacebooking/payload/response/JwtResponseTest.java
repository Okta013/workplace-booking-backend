package com.anikeeva.traineeship.workplacebooking.payload.response;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtResponseTest {

    @Test
    void testJwtResponseConstructorWithAllParameters() {
        String token = "dummyToken";
        String email = "user@example.com";
        List<String> roles = Arrays.asList("USER", "ADMIN");
        String type = "Bearer";

        JwtResponse jwtResponse = new JwtResponse(token, email, roles, type);

        assertNotNull(jwtResponse);
        assertEquals(token, jwtResponse.token());
        assertEquals(email, jwtResponse.email());
        assertEquals(roles, jwtResponse.roles());
        assertEquals(type, jwtResponse.type());
    }

    @Test
    void testJwtResponseConstructorWithDefaultType() {
        String token = "dummyToken";
        String email = "user@example.com";
        List<String> roles = Arrays.asList("USER", "ADMIN");

        JwtResponse jwtResponse = new JwtResponse(token, email, roles);

        assertNotNull(jwtResponse);
        assertEquals(token, jwtResponse.token());
        assertEquals(email, jwtResponse.email());
        assertEquals(roles, jwtResponse.roles());
        assertEquals("Bearer", jwtResponse.type());
    }

    @Test
    void testJwtResponseConstructorWithNullToken() {
        String email = "user@example.com";
        List<String> roles = Collections.singletonList("USER");

        JwtResponse jwtResponse = new JwtResponse(null, email, roles);

        assertNotNull(jwtResponse);
        assertNull(jwtResponse.token());
        assertEquals(email, jwtResponse.email());
        assertEquals(roles, jwtResponse.roles());
        assertEquals("Bearer", jwtResponse.type());
    }

    @Test
    void testJwtResponseConstructorWithNullEmail() {
        String token = "dummyToken";
        List<String> roles = Collections.singletonList("USER");

        JwtResponse jwtResponse = new JwtResponse(token, null, roles);

        assertNotNull(jwtResponse);
        assertEquals(token, jwtResponse.token());
        assertNull(jwtResponse.email());
        assertEquals(roles, jwtResponse.roles());
        assertEquals("Bearer", jwtResponse.type());
    }

    @Test
    void testJwtResponseImmutable() {
        String token = "dummyToken";
        String email = "user@example.com";
        List<String> roles = Collections.singletonList("USER");
        JwtResponse jwtResponse = new JwtResponse(token, email, roles);

        assertThrows(UnsupportedOperationException.class, () -> jwtResponse.roles().add("ADMIN"));
    }

    @Test
    void testEquality() {
        String token = "dummyToken";
        String email = "user@example.com";
        List<String> roles = Collections.singletonList("USER");

        JwtResponse jwtResponse1 = new JwtResponse(token, email, roles);
        JwtResponse jwtResponse2 = new JwtResponse(token, email, roles);

        assertEquals(jwtResponse1, jwtResponse2);
        assertEquals(jwtResponse1.hashCode(), jwtResponse2.hashCode());
    }
}
