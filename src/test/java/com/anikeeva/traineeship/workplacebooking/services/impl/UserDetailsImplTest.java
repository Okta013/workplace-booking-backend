package com.anikeeva.traineeship.workplacebooking.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserDetailsImpl.class})
@ExtendWith(SpringExtension.class)
class UserDetailsImplTest {
    @Autowired
    private UserDetailsImpl userDetailsImpl;

    @Test
    void testBuild() {

        UserEntity userEntity = new UserEntity();
        userEntity.setIsDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setFullName("Dr Jane Doe");
        UUID id = UUID.randomUUID();
        userEntity.setId(id);
        userEntity.setPassword("password");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRoles(new HashSet<>());

        UserDetailsImpl actualBuildResult = UserDetailsImpl.build(userEntity);

        assertInstanceOf(List.class, actualBuildResult.getAuthorities());
        assertEquals("6625550144", actualBuildResult.getPhoneNumber());
        assertEquals("Dr Jane Doe", actualBuildResult.getFullName());
        assertEquals("password", actualBuildResult.getPassword());
        assertEquals("jane.doe@example.org", actualBuildResult.getEmail());
        assertEquals("jane.doe@example.org", actualBuildResult.getUsername());
        assertFalse(actualBuildResult.isAccountNonLocked());
        assertFalse(actualBuildResult.isEnabled());
        assertTrue(actualBuildResult.isAccountNonExpired());
        assertTrue(actualBuildResult.isCredentialsNonExpired());
        assertTrue(actualBuildResult.isDeleted());
        assertSame(id, actualBuildResult.getId());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetailsImpl.isAccountNonLocked());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetailsImpl.isEnabled());
    }

    @Test
    void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl();
        UserDetailsImpl userDetailsImpl2 = new UserDetailsImpl();

        assertEquals(userDetailsImpl, userDetailsImpl2);
        int notExpectedHashCodeResult = userDetailsImpl.hashCode();
        assertNotEquals(notExpectedHashCodeResult, userDetailsImpl2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl();

        assertEquals(userDetailsImpl, userDetailsImpl);
        int expectedHashCodeResult = userDetailsImpl.hashCode();
        assertEquals(expectedHashCodeResult, userDetailsImpl.hashCode());
    }

    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
        UUID id = UUID.randomUUID();
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(id, "Dr Jane Doe", "6625550144", "jane.doe@example.org",
                "password", true, new ArrayList<>(), true);

        assertNotEquals(userDetailsImpl, new UserDetailsImpl());
    }

    @Test
    void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getIsDeleted()).thenReturn(true);
        when(userEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(userEntity.getFullName()).thenReturn("Dr Jane Doe");
        when(userEntity.getPassword()).thenReturn("password");
        when(userEntity.getPhoneNumber()).thenReturn("6625550144");
        when(userEntity.getRoles()).thenReturn(new HashSet<>());
        when(userEntity.getId()).thenReturn(UUID.randomUUID());
        UserDetailsImpl buildResult = UserDetailsImpl.build(userEntity);

        assertNotEquals(buildResult, new UserDetailsImpl());
    }
}
