package com.anikeeva.traineeship.workplacebooking.dto;

import com.anikeeva.traineeship.workplacebooking.entities.enums.EnumRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class RoleForRoleDTOTest {

    @InjectMocks
    private RoleForRoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleDTO = new RoleForRoleDTO();
    }

    @Test
    void testSettersAndGetters() {
        Integer id = 1;
        EnumRole role = EnumRole.ROLE_ADMIN;

        roleDTO.setId(id);
        roleDTO.setRoles(role);

        assertThat(roleDTO.getId()).isEqualTo(id);
        assertThat(roleDTO.getRoles()).isEqualTo(role);
    }

    @Test
    void testAllArgsConstructor() {
        Integer id = 2;
        EnumRole role = EnumRole.ROLE_USER;

        RoleForRoleDTO roleDTOWithArgs = new RoleForRoleDTO(id, role);

        assertThat(roleDTOWithArgs.getId()).isEqualTo(id);
        assertThat(roleDTOWithArgs.getRoles()).isEqualTo(role);
    }

    @Test
    void testNoArgsConstructor() {
        RoleForRoleDTO roleDTO = new RoleForRoleDTO();

        assertThat(roleDTO.getId()).isNull();
        assertThat(roleDTO.getRoles()).isNull();
    }

    @Test
    void testSetId() {
        roleDTO.setId(5);

        assertThat(roleDTO.getId()).isEqualTo(5);
    }

    @Test
    void testSetRoles() {
        roleDTO.setRoles(EnumRole.ROLE_USER);

        assertThat(roleDTO.getRoles()).isEqualTo(EnumRole.ROLE_USER);
    }

    @Test
    void testEnumRoleIsNull() {
        roleDTO.setRoles(null);

        assertThat(roleDTO.getRoles()).isNull();
    }

    @Test
    void testRoleIsAdmin() {
        roleDTO.setRoles(EnumRole.ROLE_ADMIN);

        assertThat(roleDTO.getRoles()).isEqualTo(EnumRole.ROLE_ADMIN);
    }
}
