package com.anikeeva.traineeship.workplacebooking.payload.request;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangePasswordRequestTest {

    @Test
    public void testToString() {
        String oldPassword = "oldPassword123";
        String newPassword = "newPassword456";

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword, newPassword);

        String toStringOutput = changePasswordRequest.toString();

        assertTrue(toStringOutput.contains("oldPassword123"));
        assertTrue(toStringOutput.contains("newPassword456"));
    }
}
