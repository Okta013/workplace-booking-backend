package com.anikeeva.traineeship.workplacebooking.repositories;

import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    List<UserEntity> findByIsDeleted(Boolean status);

    List<UserEntity> findByFullNameOrPhoneNumberOrEmail(String fullName, String phoneNumber, String email);

    Optional<UserEntity> findByEmail(String email);

    UserEntity findByFullName(String fullName);
}