package com.anikeeva.traineeship.workplacebooking.repositories;

import com.anikeeva.traineeship.workplacebooking.entities.Role;
import com.anikeeva.traineeship.workplacebooking.entities.enums.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoles(EnumRole roles);
}
