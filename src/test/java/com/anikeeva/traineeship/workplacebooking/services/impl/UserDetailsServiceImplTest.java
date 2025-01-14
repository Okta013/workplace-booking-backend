package com.anikeeva.traineeship.workplacebooking.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.anikeeva.traineeship.workplacebooking.entities.Role;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import com.anikeeva.traineeship.workplacebooking.entities.enums.EnumRole;
import com.anikeeva.traineeship.workplacebooking.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@ContextConfiguration(classes = {UserDetailsServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername_UserDisabled_ShouldThrowException() {
        UserEntity userEntity = new UserEntity();
        userEntity.setIsDeleted(true);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setFullName("Dr Jane Doe");
        userEntity.setId(UUID.randomUUID());
        userEntity.setPassword("password");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRoles(new HashSet<>());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        assertThrows(DisabledException.class, () -> userDetailsServiceImpl.loadUserByUsername("jane.doe@example.org"));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }

    @Test
    void testLoadUserByUsername_UserEnabled_ShouldReturnUserDetails() {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getIsDeleted()).thenReturn(false);
        when(userEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(userEntity.getFullName()).thenReturn("Dr Jane Doe");
        when(userEntity.getPassword()).thenReturn("password");
        when(userEntity.getPhoneNumber()).thenReturn("6625550144");
        when(userEntity.getRoles()).thenReturn(new HashSet<>());
        UUID randomUUID = UUID.randomUUID();
        when(userEntity.getId()).thenReturn(randomUUID);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("jane.doe@example.org");

        assertInstanceOf(UserDetailsImpl.class, userDetails);
        assertEquals("jane.doe@example.org", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertFalse(((UserDetailsImpl) userDetails).isDeleted());
        assertEquals("Dr Jane Doe", ((UserDetailsImpl) userDetails).getFullName());
        assertEquals(randomUUID, ((UserDetailsImpl) userDetails).getId());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().isEmpty());
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ShouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsServiceImpl.loadUserByUsername("jane.doe@example.org"));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }

    @Test
    void testLoadUserByUsername_UserWithRoles_ShouldReturnUserDetailsWithRoles() {
        Role role = new Role();
        role.setId(2);
        role.setRoles(EnumRole.ROLE_USER);

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getIsDeleted()).thenReturn(false);
        when(userEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(userEntity.getFullName()).thenReturn("Dr Jane Doe");
        when(userEntity.getPassword()).thenReturn("password");
        when(userEntity.getPhoneNumber()).thenReturn("6625550144");
        when(userEntity.getRoles()).thenReturn(roles);
        UUID randomUUID = UUID.randomUUID();
        when(userEntity.getId()).thenReturn(randomUUID);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("jane.doe@example.org");

        assertInstanceOf(UserDetailsImpl.class, userDetails);
        assertEquals(1, userDetails.getAuthorities().size());
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }

    @Test
    void testLoadUserByUsername_UserWithMultipleRoles_ShouldReturnUserDetailsWithMultipleRoles() {
        Role adminRole = new Role();
        adminRole.setId(3);
        adminRole.setRoles(EnumRole.ROLE_ADMIN);

        Role userRole = new Role();
        userRole.setId(2);
        userRole.setRoles(EnumRole.ROLE_USER);

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);

        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getIsDeleted()).thenReturn(false);
        when(userEntity.getEmail()).thenReturn("jane.doe@example.org");
        when(userEntity.getFullName()).thenReturn("Dr Jane Doe");
        when(userEntity.getPassword()).thenReturn("password");
        when(userEntity.getPhoneNumber()).thenReturn("6625550144");
        when(userEntity.getRoles()).thenReturn(roles);
        UUID randomUUID = UUID.randomUUID();
        when(userEntity.getId()).thenReturn(randomUUID);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("jane.doe@example.org");

        assertInstanceOf(UserDetailsImpl.class, userDetails);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
    }
}
