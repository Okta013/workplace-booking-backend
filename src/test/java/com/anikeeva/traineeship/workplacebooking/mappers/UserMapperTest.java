package com.anikeeva.traineeship.workplacebooking.mappers;

import com.anikeeva.traineeship.workplacebooking.dto.UserForAdminDTO;
import com.anikeeva.traineeship.workplacebooking.dto.UserForUserDTO;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UserMapperTest {
    UserMapper userMapper = new UserMapperImpl();

    @Test
    public void toUserForUserDTO(){
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity(userId, "Aaa Bbb Xxx",
                "+79276547890", "abx@gmail.com", "password", false);
        UserForUserDTO expectedUserForUserDTO = new UserForUserDTO("Aaa Bbb Xxx",
                "+79276547890", "abx@gmail.com", false);

        UserForUserDTO actualUserForUserDTO = userMapper.toUserForUserDTO(userEntity);

        Assertions.assertEquals(expectedUserForUserDTO, actualUserForUserDTO);
    }

    @Test
    public void toUserForUserDTOWithNull(){
        UserForUserDTO actualUserForUserDTO = userMapper.toUserForUserDTO(null);

        Assertions.assertNull(actualUserForUserDTO);
    }

    @Test
    public void toUserForAdminDTO(){
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity(userId, "Aaa Bbb Xxx",
                "+79276547890", "abx@gmail.com", "password",false);
        UserForAdminDTO expectedUserForAdminDTO = new UserForAdminDTO(userId,"Aaa Bbb Xxx",
                "+79276547890", "abx@gmail.com", false, false);

        UserForAdminDTO actualUserForAdminDTO = userMapper.toUserForAdminDTO(userEntity);

        Assertions.assertEquals(expectedUserForAdminDTO, actualUserForAdminDTO);
    }

    @Test
    public void toUserForAdminDTOWithNull(){
        UserForAdminDTO actualUserForAdminDTO = userMapper.toUserForAdminDTO(null);

        Assertions.assertNull(actualUserForAdminDTO);
    }

    @Test
    public void toUserEntity() {
        UUID userId = UUID.randomUUID();
        UserForAdminDTO userForAdminDTO = new UserForAdminDTO(userId, "Aaa Bbb Xxx",
                "+79276547890", "abx@gmail.com", false, false);
        UserEntity expectedUserEntity = new UserEntity(userId, "Aaa Bbb Xxx",
                "+79276547890", "abx@gmail.com", "password",false);

        UserEntity actualUserEntity = userMapper.toUserEntity(userForAdminDTO);

        Assertions.assertEquals(expectedUserEntity.getFullName(), actualUserEntity.getFullName());
        Assertions.assertEquals(expectedUserEntity.getPhoneNumber(), actualUserEntity.getPhoneNumber());
        Assertions.assertEquals(expectedUserEntity.getEmail(), actualUserEntity.getEmail());
        Assertions.assertEquals(expectedUserEntity.getIsDeleted(), actualUserEntity.getIsDeleted());
    }

    @Test
    public void toUserEntityWithNull(){
        UserForAdminDTO userForAdminDTO = null;

        UserEntity actualUserEntity = userMapper.toUserEntity(userForAdminDTO);

        Assertions.assertNull(actualUserEntity);
    }

    @Test
    public void updateUserEntityFromUserForAdminDTO() {
        UUID userId = UUID.randomUUID();
        UserForAdminDTO userForAdminDTO = new UserForAdminDTO(userId, "Aaa Bbb Xxx",
                "+79276547890", "abx@gmail.com", false, false);
        UserEntity userEntity = new UserEntity(userId, "Some Name",
                "+79276547890", "abx@gmail.com", "password",false);
        UserEntity expectedUserEntity = new UserEntity(userId, "Aaa Bbb Xxx",
                "+79276547890", "abx@gmail.com", "password",false);

        userMapper.updateUserEntityFromUserForAdminDTO(userForAdminDTO, userEntity);

        Assertions.assertEquals(userEntity, expectedUserEntity);
    }
}